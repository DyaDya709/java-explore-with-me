package ru.practicum.events.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.service.CommentServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/comments/event/{eventId}")
@RequiredArgsConstructor
public class CommentControllerPublic {
    private final CommentServicePublic commentServicePublic;

    @GetMapping()
    List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return commentServicePublic.getAllCommentsByEventId(eventId, from, size);
    }
}
