package ru.practicum.events.comment.service;

import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.dto.UpdateCommentAdminDto;

import java.util.List;

public interface CommentServiceAdmin {
    CommentDto createComment(InputCommentDto inputCommentDto);

    CommentDto updateComment(Long commentId, UpdateCommentAdminDto updateComment);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    void deleteComment(Long commentId);
}
