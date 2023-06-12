package ru.practicum.events.event.service.impl;

import exolrerwithme.HttpClient;
import explorewithme.dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.event.dto.EventFullDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.mapper.EventMapper;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;
import ru.practicum.events.event.service.EventServicePublic;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.events.request.model.RequestStatus;
import ru.practicum.exception.type.BadRequestException;
import ru.practicum.exception.type.ResourceNotFoundException;
import ru.practicum.util.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServicePublicImpl implements EventServicePublic {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EventRepository eventRepository;
    private final ProcessingEvents processingEvents;
    private final HttpClient client;
    @Value("${app.name}")
    private String appName;

    @Override
    public List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        log.info("Получен запрос на получение всех событий (публичный)");
        HitDto hitDto = HitDto.builder()
                .app(appName)
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
        client.createHit(hitDto);
        Pageable page = PageRequest.of(from / size, size);
        LocalDateTime newRangeStart = null;
        if (rangeStart != null) {
            newRangeStart = DateFormatter.formatDate(rangeStart);
        }
        LocalDateTime newRangeEnd = null;
        if (rangeEnd != null) {
            newRangeEnd = DateFormatter.formatDate(rangeEnd);
        }
        List<Event> events = eventRepository.findAllByPublic(text, categories, paid, newRangeStart, newRangeEnd, page);
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
        List<Event> newEvents = processingEvents.confirmedRequests(eventsAddViews);
        if (!onlyAvailable) {
            return newEvents.stream().filter(e -> e.getParticipantLimit() >= e.getConfirmedRequests())
                    .map(EventMapper::toShortDto).collect(Collectors.toList());
        }
        return newEvents.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        log.info("Получен запрос на получение события по id= {} (публичный)", id);
        HitDto hitDto = createHitDtoToStats(request);
        client.createHit(hitDto);
        Event event = eventRepository.findEventByIdAndStateIs(id, EventState.PUBLISHED).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));
        addEventConfirmedRequestsAndViews(event, request);
        return EventMapper.toDto(event);
    }

    private HitDto createHitDtoToStats(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
        return hitDto;
    }

    private void addEventConfirmedRequestsAndViews(Event event, HttpServletRequest request) {
        long count = processingEvents.countAllRequestsForOneEvent(event, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(count);
        long views = processingEvents.searchViews(event, request);
        event.setViews(views);
    }
}
