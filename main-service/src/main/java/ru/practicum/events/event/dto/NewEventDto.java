package ru.practicum.events.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Поле annotation должно быть заполнено")
    @Size(min = 20, max = 2000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 2000")
    String annotation; //Краткое описание события
    @NotNull(message = "category не должно быть пустым")
    Long category;
    @NotBlank(message = "Поле description должно быть заполнено")
    @Size(min = 20, max = 7000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 7000")
    String description; //Полное описание события
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Invalid date format")
    String eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    LocationDto location; //Широта и долгота места проведения события
    @org.springframework.beans.factory.annotation.Value("false")
    boolean paid; // Нужно ли оплачивать участие
    @org.springframework.beans.factory.annotation.Value("0")
    @PositiveOrZero
    Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @org.springframework.beans.factory.annotation.Value("true")
    boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    @NotBlank(message = "Поле title должно быть заполнено")
    String title; // example: Знаменитое шоу 'Летающая кукуруза' Заголовок
}
