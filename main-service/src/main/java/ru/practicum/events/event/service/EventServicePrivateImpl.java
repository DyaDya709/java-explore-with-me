package ru.practicum.events.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.events.event.dto.EventDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.dto.NewEventDto;
import ru.practicum.events.event.dto.UpdateEventUserRequest;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.storage.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        return null;
    }

    @Override
    public EventDto addPrivateEventByUserId(Long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventDto getPrivateEventByIdAndByUserId(Long userId, Long eventId, HttpServletRequest request) {
        return null;
    }

    @Override
    public EventDto updatePrivateEventByIdAndByUserId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getAllPrivateEventsByRequests(Long userId, Long eventId, HttpServletRequest request) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest, HttpServletRequest request) {
        return null;
    }
}
