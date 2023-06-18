package ru.practicum.events.comment.service;

import ru.practicum.events.comment.dto.CommentDto;

import java.util.List;

public interface CommentServicePublic {
    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);
}
