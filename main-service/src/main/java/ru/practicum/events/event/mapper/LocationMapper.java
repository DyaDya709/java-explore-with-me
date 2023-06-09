package ru.practicum.events.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.events.event.dto.LocationDto;
import ru.practicum.events.event.model.location.Location;

@UtilityClass
public class LocationMapper {
    public Location toEntity(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
