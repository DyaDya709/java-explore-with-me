package ru.practicum.events.event.service.impl;

import exolrerwithme.HttpClient;
import explorewithme.dto.StatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.request.model.Request;
import ru.practicum.events.request.model.RequestStatus;
import ru.practicum.events.request.storage.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessingEvents {
    private final RequestRepository requestRepository;
    private final HttpClient client;

    public List<Event> addViewsInEventsList(List<Event> events, HttpServletRequest request) {
        List<String> uris = events.stream().map(e -> request.getRequestURI() + "/" + e.getId()).collect(Collectors.toList());
        LocalDateTime start = findStartDateTime(events);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<StatDto> stats = client.getStats(start, end, uris, true);
        fillEventViews(events, stats, request.getRequestURI());
        return events;
    }

    private void fillEventViews(List<Event> events, List<StatDto> stats, String baseUri) {
        if (!stats.isEmpty()) {
            Map<String, Long> statsByUri = stats.stream()
                    .collect(Collectors.groupingBy(StatDto::getUri, Collectors.summingLong(v -> v.getHits().longValue())));
            events.forEach(e -> {
                Long views = statsByUri.get(baseUri + "/" + e.getId());
                if (views != null) {
                    e.setViews(views);
                }
            });
        } else {
            events.stream().forEach(e -> e.setViews(0L));
        }
    }

    public List<Event> confirmedRequests(List<Event> events) {
        Map<Event, Long> requestsPerEvent = requestRepository.findAllByEventInAndStatus(events, RequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(Request::getEvent, Collectors.counting()));
        List<Event> newEvents = new ArrayList<>();
        if (!requestsPerEvent.isEmpty()) {
            for (Event e : events) {
                Long count = requestsPerEvent.get(e);
                e.setConfirmedRequests(count);
                newEvents.add(e);
            }
        } else {
            newEvents.addAll(events);
            newEvents.stream().forEach(e -> e.setConfirmedRequests(0L));
        }
        return newEvents;
    }

    public long getCountAllRequestsForOneEvent(Event event, RequestStatus status) {
        return requestRepository.countRequestByEventAndStatus(event, status);
    }

    public long searchViews(Event event, HttpServletRequest request) {
        LocalDateTime date = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0, 1));
        LocalDateTime start = event.getPublishedOn() == null ? date : event.getPublishedOn();
        List<StatDto> stats = client.getStats(start, LocalDateTime.now(), List.of(request.getRequestURI()), true);
        Long hits = stats.stream().map(StatDto::getHits).reduce(0L, Long::sum);
        return hits;
    }

    private LocalDateTime findStartDateTime(List<Event> events) {
        List<Event> sortedEvents = events.stream()
                .sorted(Comparator.comparing(Event::getPublishedOn, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        LocalDateTime start = LocalDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.of(0, 0, 1));
        if (!sortedEvents.isEmpty()) {
            Event event = sortedEvents.get(0);
            if (event.getPublishedOn() != null) {
                start = event.getPublishedOn();
            }
        }
        return start;
    }
}
