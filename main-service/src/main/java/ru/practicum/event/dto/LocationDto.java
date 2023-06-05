package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDto {
    float lat; // example: 55.754167 - Широта
    float lon; // example: 37.62 - Долгота
}
