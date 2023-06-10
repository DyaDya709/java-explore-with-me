package ru.practicum.events.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.events.event.dto.EventShortDto;

import java.util.List;

@Builder
@Data
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    boolean pinned; // Закреплена ли подборка на главной странице сайта example: true
    String title; // Заголовок подборки example: Летние концерты
}
