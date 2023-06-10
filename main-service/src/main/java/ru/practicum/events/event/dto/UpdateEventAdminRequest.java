package ru.practicum.events.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.event.model.ActionState;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 2000")
    String annotation; // example: Сап прогулки по рекам и каналам – это возможность увидеть Практикбург с другого ракурсаНовая аннотация
    Long category; // Новая категория
    @Size(min = 20, max = 7000, message = "Минимальное кол-во символов для описания: 20. Максимальное: 7000")
    String description; // Новое описание
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Invalid date format")
    String eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    LocationDto location; //Широта и долгота места проведения события
    Boolean paid; // Нужно ли оплачивать участие
    @PositiveOrZero
    Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    ActionState stateAction;
    @Size(min = 5, max = 255, message = "Минимальное кол-во символов для описания: 5. Максимальное: 255")
    String title; // example: Знаменитое шоу 'Летающая кукуруза' Заголовок
}
