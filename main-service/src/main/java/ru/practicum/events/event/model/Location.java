package ru.practicum.events.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Embeddable
public class Location {
    private float lat;
    private float lon;
}
