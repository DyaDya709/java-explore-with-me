package ru.practicum.events.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.events.event.dto.EventDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.dto.NewEventDto;
import ru.practicum.events.event.dto.UpdateEventUserRequest;
import ru.practicum.events.event.mapper.EventMapper;
import ru.practicum.events.event.mapper.LocationMapper;
import ru.practicum.events.event.model.ActionState;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.CustomBadRequestException;
import ru.practicum.exception.CustomConflictRequestException;
import ru.practicum.exception.CustomForbiddenEventException;
import ru.practicum.exception.CustomResourceNotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final ProcessingEvents processingEvents;
    private final UserRepository userRepository;

    @Override
    public List<EventShortDto> getAllPrivateEventsByUserId(Long userId, int from, int size, HttpServletRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new CustomResourceNotFoundException("Пользователь c id = " + userId + " не найден");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmedRequests(eventsAddViews);
        return newEvents.stream()
                .map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    public EventDto addPrivateEventByUserId(Long userId, NewEventDto newEventDto) {
        checkEventDate(DateFormatter.formatDate(newEventDto.getEventDate()));
        User user = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Long views = 0L;
        Long confirmedRequests = 0L;
        Event event = EventMapper.newEventDtoToCreateEvent(newEventDto, user, category, views, confirmedRequests);
        return getEventFullDto(event);
    }

    @Override
    public EventDto getPrivateEventByIdAndByUserId(Long userId, Long eventId, HttpServletRequest request) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        checkOwnerEvent(event, user);
        addEventConfirmedRequestsAndViews(event, request);
        return EventMapper.toDto(event);
    }

    @Override
    public EventDto updatePrivateEventByIdAndByUserId(Long userId, Long eventId, UpdateEventUserRequest updateEvent, HttpServletRequest request) {
        if (updateEvent.getEventDate() != null) {
            checkEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        Event event = getEventById(eventId);
        User user = getUserById(userId);
        checkOwnerEvent(event, user);
        eventAvailability(event);
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = getCategoryById(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(DateFormatter.formatDate(updateEvent.getEventDate()));
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(LocationMapper.toEntity(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            event.setState(determiningTheStatusForEvent(updateEvent.getStateAction()));
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmedRequestsAndViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        return getEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getAllPrivateEventsByRequests(Long userId, Long eventId, HttpServletRequest request) {
        try {
            Event event = getEventById(eventId);
            User user = getUserById(userId);
            checkOwnerEvent(event, user);
            List<Request> requests = requestRepository.findAllByEvent(event);
            return requests.stream().map(RequestMapper::requestToParticipationRequestDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomBadRequestException("Некорректный запрос получения списка запросов на участие в текущем событии");
        }
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest, HttpServletRequest request) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);
        checkOwnerEvent(event, user);
        if (event.getState().equals(EventState.PUBLISHED)) {
            addEventConfirmedRequestsAndViews(event, request);
        } else {
            event.setViews(0L);
            event.setConfirmedRequests(0L);
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new CustomForbiddenEventException("Достигнут лимит по заявкам на данное событие с id= " + eventId);
        }
        List<Request> requests = getAllRequestsContainsIds(eventRequest.getRequestIds());
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        } else if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            return considerationOfRequests(event, requests);
        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
            List<ParticipationRequestDto> rejectedRequests = addRejectedAllRequests(requests);
            result.getRejectedRequests().addAll(rejectedRequests);
            return result;
        }
        return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null) {
            LocalDateTime timeNow = LocalDateTime.now().plusHours(2L);
            if (eventDate.isBefore(timeNow)) {
                throw new CustomForbiddenEventException("Событие должно содержать дату, которая еще не наступила. " +
                        "Value: " + eventDate);
            }
        }
    }

    private void checkOwnerEvent(Event event, User user) {
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new CustomForbiddenEventException("Событие с id=" + event.getId() + " не принадлежит пользователю с id=" + user.getId());
        }
    }

    private EventState determiningTheStatusForEvent(ActionState stateAction) {
        if (stateAction.equals(ActionState.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        } else if (stateAction.equals(ActionState.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        } else if (stateAction.equals(ActionState.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        } else if (stateAction.equals(ActionState.REJECT_EVENT)) {
            return EventState.CANCELED;
        } else {
            throw new CustomBadRequestException("Статус не соответствует модификатору доступа");
        }
    }

    private void eventAvailability(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new CustomForbiddenEventException("Статус события не позволяет редоктировать событие, статус: " + event.getState());
        }
    }

    private List<ParticipationRequestDto> addRejectedAllRequests(List<Request> requests) {
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request req : requests) {
            if (!req.getStatus().equals(RequestStatus.PENDING)) {
                throw new CustomConflictRequestException("Статус заявки " + req.getId() + " не позволяет ее одобрить, статус равен " + req.getStatus());
            }
            req.setStatus(RequestStatus.REJECTED);
            requestRepository.save(req);
            rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(req));
        }
        return rejectedRequests;
    }

    private List<Request> getAllRequestsContainsIds(List<Long> requestIds) {
        return requestRepository.findAllByIdIsIn(requestIds);
    }

    private EventRequestStatusUpdateResult considerationOfRequests(Event event, List<Request> requests) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        for (Request req : requests) {
            if (!req.getStatus().equals(RequestStatus.PENDING)) {
                throw new CustomConflictRequestException("Статус заявки " + req.getId() + " не позволяет ее одобрить, статус равен " + req.getStatus());
            }
            if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                req.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.requestToParticipationRequestDto(req));
                requestRepository.save(req);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
            } else {
                req.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.requestToParticipationRequestDto(req));
                requestRepository.save(req);
            }
        }
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private EventDto getEventFullDto(Event event) {
        try {
            return EventMapper.toDto(eventRepository.save(event));
        } catch (DataAccessException e) {
            throw new CustomResourceNotFoundException("База данных недоступна");
        } catch (Exception e) {
            throw new CustomBadRequestException("Запрос на добавлении события " + event + " составлен не корректно ");
        }
    }

    private void addEventConfirmedRequestsAndViews(Event event, HttpServletRequest request) {
        long count = processingEvents.confirmedRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new CustomResourceNotFoundException("Категория c id = " + id + " не найдена"));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new CustomResourceNotFoundException("Пользователь c id = " + id + " не найден"));
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> new CustomResourceNotFoundException("Событие c id = " + id + " не найдено"));
    }
}
