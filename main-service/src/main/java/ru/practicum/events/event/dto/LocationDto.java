package ru.practicum.events.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    float lat; // example: 55.754167 - Широта
    float lon; // example: 37.62 - Долгота
}
