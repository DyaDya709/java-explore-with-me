package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class NewCategoryDto {
    @Size(max = 255)
    @NotBlank
    private String name; // Название категории example: Концерты
}
