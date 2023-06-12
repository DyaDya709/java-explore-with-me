package ru.practicum.events.event.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;

@Builder
@Value
public class NewEventDto {
    @NotBlank(message = "Поле annotation должно быть заполнено")
    @Size(min = 20, max = 2000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 2000")
    String annotation;
    @NotNull(message = "category не должно быть пустым")
    Long category;
    @NotBlank(message = "Поле description должно быть заполнено")
    @Size(min = 20, max = 7000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 7000")
    String description;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Invalid date format")
    String eventDate;
    LocationDto location; //Широта и долгота места проведения события
    Boolean paid; // Нужно ли оплачивать участие
    @PositiveOrZero
    Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    @NotBlank(message = "Поле title должно быть заполнено")
    @Size(min = 3, max = 120)
    String title; // example: Знаменитое шоу 'Летающая кукуруза' Заголовок
}
