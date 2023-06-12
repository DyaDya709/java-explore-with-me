package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewUserRequest {
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    String email;
    @Size(min = 2, max = 250)
    @NotBlank
    String name;
}
