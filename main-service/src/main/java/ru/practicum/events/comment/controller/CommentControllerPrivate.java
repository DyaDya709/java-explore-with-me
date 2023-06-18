package ru.practicum.events.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.service.CommentServicePrivate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/private/comments")
@RequiredArgsConstructor
public class CommentControllerPrivate {
    private final CommentServicePrivate commentServicePrivate;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@Validated @RequestBody InputCommentDto inputCommentDto) {
        return commentServicePrivate.createComment(inputCommentDto);
    }

    @PatchMapping("/{commentId}")
    CommentDto updateComment(@PathVariable Long commentId,
                             @Validated @RequestBody InputCommentDto inputCommentDto) {
        return commentServicePrivate.updateComment(commentId, inputCommentDto);
    }

    @GetMapping("/{commentId}/user/{userId}")
    CommentDto getCommentById(@PathVariable Long commentId,
                              @PathVariable Long userId) {
        return commentServicePrivate.getCommentById(commentId, userId);
    }

    @GetMapping("/event/{eventId}/user/{userId}")
    List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId,
                                             @PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return commentServicePrivate.getAllCommentsByEventId(eventId, userId, from, size);
    }

    @DeleteMapping("/{commentId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable Long commentId,
                       @PathVariable Long userId) {
        commentServicePrivate.deleteComment(commentId, userId);
    }
}
