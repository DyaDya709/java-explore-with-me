package ru.practicum.events.comment.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class UpdateCommentAdminDto {
    @NotBlank
    @Size(max = 1500)
    String text;
    @NotNull
    Long userId;
    @NotNull
    Long eventId;
    CommentStateDto commentStateDto;
}
