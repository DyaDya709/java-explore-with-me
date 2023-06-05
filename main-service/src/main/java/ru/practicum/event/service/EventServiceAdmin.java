package ru.practicum.event.service;

import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServiceAdmin {
    List<EventDto> getAllEventsForAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size, HttpServletRequest request);

    EventDto updateEventById(Long eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request);
}
