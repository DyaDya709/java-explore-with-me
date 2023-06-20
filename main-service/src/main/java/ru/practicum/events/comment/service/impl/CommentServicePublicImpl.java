package ru.practicum.events.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.mapper.CommentMapper;
import ru.practicum.events.comment.model.Comment;
import ru.practicum.events.comment.model.CommentState;
import ru.practicum.events.comment.service.CommentServicePublic;
import ru.practicum.events.comment.storage.CommentRepository;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.type.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServicePublicImpl implements CommentServicePublic {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        Event event = getEventById(eventId);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEventAndStateIsNot(event, CommentState.CANCELED, pageable);
        return comments.stream().map(CommentMapper::commentToCommentDto).collect(Collectors.toList());
    }

    private Event getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));
        return event;
    }
}
