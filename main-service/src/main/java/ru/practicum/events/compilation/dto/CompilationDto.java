package ru.practicum.events.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.events.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}
