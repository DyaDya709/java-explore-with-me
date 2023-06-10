package ru.practicum.events.event.service;

import ru.practicum.events.event.dto.EventDto;
import ru.practicum.events.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {
    List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    EventDto getPublicEventById(Long id, HttpServletRequest request);
}
