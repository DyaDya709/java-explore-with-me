package ru.practicum.events.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.events.event.dto.EventFullDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.dto.NewEventDto;
import ru.practicum.events.event.dto.UpdateEventUserRequest;
import ru.practicum.events.event.dto.stateDto.ActionStateDto;
import ru.practicum.events.event.mapper.EventMapper;
import ru.practicum.events.event.mapper.LocationMapper;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;
import ru.practicum.events.event.service.EventServicePrivate;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.events.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.events.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.events.request.dto.ParticipationRequestDto;
import ru.practicum.events.request.dto.RequestStatusDto;
import ru.practicum.events.request.mapper.RequestMapper;
import ru.practicum.events.request.model.Request;
import ru.practicum.events.request.model.RequestStatus;
import ru.practicum.events.request.storage.RequestRepository;
import ru.practicum.exception.type.*;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.users.model.User;
import ru.practicum.users.storage.UserRepository;

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
            throw new ResourceNotFoundException("Пользователь c id = " + userId + " не найден");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmedRequests(eventsAddViews);
        return newEvents.stream()
                .map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto createPrivateEventByUserId(Long userId, NewEventDto newEventDto) {
        checkEventDate(DateFormatter.formatDate(newEventDto.getEventDate()));
        User user = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Long views = 0L;
        Long confirmedRequests = 0L;
        Event event = EventMapper.createEvent(newEventDto, user, category, views, confirmedRequests);
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto getPrivateEventByIdAndByUserId(Long userId, Long eventId, HttpServletRequest request) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        checkOwnerEvent(event, user);
        addEventConfirmedRequestsAndViews(event, request);
        return EventMapper.toDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updatePrivateEventByIdAndByUserId(Long userId, Long eventId, UpdateEventUserRequest updateEvent, HttpServletRequest request) {
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
            event.setState(calculateStatusForEvent(updateEvent.getStateAction()));
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
            throw new BadRequestException("Некорректный запрос получения списка запросов на участие в текущем событии");
        }
    }

    @Transactional
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
            throw new ConflictEventPublicationException("Достигнут лимит по заявкам на данное событие с id= " + eventId);
        }
        List<Request> requests = getAllRequestsContainsIds(eventRequest.getRequestIds());
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        } else if (eventRequest.getStatus().equals(RequestStatusDto.CONFIRMED)) {
            return considerationOfRequests(event, requests);
        } else if (eventRequest.getStatus().equals(RequestStatusDto.REJECTED)) {
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
                throw new ForbiddenEventException("Событие должно содержать дату, которая еще не наступила. " +
                        "Value: " + eventDate);
            }
        }
    }

    private void checkOwnerEvent(Event event, User user) {
        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new ForbiddenEventException("Событие с id=" + event.getId() + " не принадлежит пользователю с id=" + user.getId());
        }
    }

    private EventState calculateStatusForEvent(ActionStateDto stateAction) {
        if (stateAction.equals(ActionStateDto.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        } else if (stateAction.equals(ActionStateDto.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        } else if (stateAction.equals(ActionStateDto.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        } else if (stateAction.equals(ActionStateDto.REJECT_EVENT)) {
            return EventState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }

    private void eventAvailability(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictEventPublicationException("Статус события не позволяет редактировать событие, статус: " + event.getState());
        }
    }

    private List<ParticipationRequestDto> addRejectedAllRequests(List<Request> requests) {
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request req : requests) {
            if (!req.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictRequestException("Статус заявки " + req.getId() + " не позволяет ее одобрить, статус равен " + req.getStatus());
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
        long count = processingEvents.getCountAllRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        for (Request req : requests) {
            if (!req.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictRequestException("Статус заявки " + req.getId() + " не позволяет ее одобрить, статус равен " + req.getStatus());
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

    private EventFullDto getEventFullDto(Event event) {
        try {
            return EventMapper.toDto(eventRepository.save(event));
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("База данных недоступна");
        } catch (Exception e) {
            throw new BadRequestException("Запрос на добавлении события " + event + " составлен не корректно ");
        }
    }

    private void addEventConfirmedRequestsAndViews(Event event, HttpServletRequest request) {
        long count = processingEvents.getCountAllRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Категория c id = " + id + " не найдена"));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id = " + id + " не найден"));
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));
    }
}

