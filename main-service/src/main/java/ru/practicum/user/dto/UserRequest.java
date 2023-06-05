package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class UserRequest {
    @NotBlank
    @Size(max = 255)
    String email;
    @NotBlank
    @Size(max = 255)
    String name;
}
