package ru.practicum.events.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class InputCommentDto {
    @NotBlank
    @Size(max = 1500)
    String text;
    @NotNull
    Long userId;
    @NotNull
    Long eventId;
}
