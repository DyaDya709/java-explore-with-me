package ru.practicum.events.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @Size(max = 50)
    @NotBlank
    private String title;
}
