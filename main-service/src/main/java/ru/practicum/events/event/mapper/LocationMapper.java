package ru.practicum.events.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.events.event.dto.LocationDto;
import ru.practicum.events.event.model.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocationMapper {
    public static Location toEntity(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
