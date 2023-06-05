package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    Long id;
    String email;
    String name;
}
