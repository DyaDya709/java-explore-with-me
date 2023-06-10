package ru.practicum.events.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.event.dto.EventDto;
import ru.practicum.events.event.dto.UpdateEventAdminRequest;
import ru.practicum.events.event.mapper.EventMapper;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.util.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final ProcessingEvents processingEvents;

    @Override
    public List<EventDto> getAllEventsForAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size, HttpServletRequest request) {
        List<Event> events = new ArrayList<>();
        LocalDateTime newRangeStart = null;
        if (rangeStart != null) {
            newRangeStart = DateFormatter.formatDate(rangeStart);
        }
        LocalDateTime newRangeEnd = null;
        if (rangeEnd != null) {
            newRangeEnd = DateFormatter.formatDate(rangeEnd);
        }

        if (states != null) {
            events = eventRepository.findAllByAdmin(users, states, categories, newRangeStart, newRangeEnd, from, size);
            List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
            List<Event> newEvents = processingEvents.confirmedRequests(eventsAddViews);
            return newEvents.stream().map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
        } else {
            events = eventRepository.findAllByAdminAndState(users, categories, newRangeStart, newRangeEnd, from, size);
            List<Event> eventsAddViews = processingEvents.addViewsInEventsList(events, request);
            List<Event> newEvents = processingEvents.confirmedRequests(eventsAddViews);
            return newEvents.stream().map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
        }
    }

    @Override
    public EventDto updateEventById(Long eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request) {
        return null;
    }
}
