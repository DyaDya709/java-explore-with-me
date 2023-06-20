package ru.practicum.events.comment.service;

import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.InputCommentDto;

import java.util.List;

public interface CommentServicePrivate {
    CommentDto createComment(InputCommentDto inputCommentDto);

    CommentDto updateComment(Long commentId, InputCommentDto inputCommentDto);

    CommentDto getCommentById(Long commentId, Long userId);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Long userId, Integer from, Integer size);

    void deleteComment(Long commentId, Long userId);
}
