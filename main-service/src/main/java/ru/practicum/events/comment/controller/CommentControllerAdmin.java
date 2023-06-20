package ru.practicum.events.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.dto.UpdateCommentAdminDto;
import ru.practicum.events.comment.service.CommentServiceAdmin;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentServiceAdmin commentServiceAdmin;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@Validated @RequestBody InputCommentDto inputCommentDto) {
        return commentServiceAdmin.createComment(inputCommentDto);
    }

    @PatchMapping("/{commentId}")
    CommentDto updateComment(@PathVariable Long commentId,
                             @Validated @RequestBody UpdateCommentAdminDto updateComment) {
        return commentServiceAdmin.updateComment(commentId, updateComment);
    }

    @GetMapping("/{commentId}")
    CommentDto getCommentById(@PathVariable Long commentId) {
        return commentServiceAdmin.getCommentById(commentId);
    }

    @GetMapping("/event/{eventId}")
    List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return commentServiceAdmin.getAllCommentsByEventId(eventId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable Long commentId) {
        commentServiceAdmin.deleteComment(commentId);
    }
}
