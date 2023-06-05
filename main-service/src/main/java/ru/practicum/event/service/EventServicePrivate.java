package ru.practicum.event.service;

import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePrivate {
    List<EventShortDto> getAllPrivateEventsByUserId(Long userId, int from, int size, HttpServletRequest request);

    EventDto addPrivateEventByUserId(Long userId, NewEventDto newEventDto);

    EventDto getPrivateEventByIdAndByUserId(Long userId, Long eventId, HttpServletRequest request);

    EventDto updatePrivateEventByIdAndByUserId(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest, HttpServletRequest request);

    List<ParticipationRequestDto> getAllPrivateEventsByRequests(Long userId, Long eventId, HttpServletRequest request);

    EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest, HttpServletRequest request);
}
