package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class CategoryDto {
    Long id; // Идентификатор категории
    @Size(max = 255)
    @NotBlank
    String name; // example: Концерты Название категории
}
