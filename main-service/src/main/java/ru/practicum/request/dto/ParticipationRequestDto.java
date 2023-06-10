package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime created; // 2022-09-06T21:10:05.432 Дата и время создания заявки
    Long event; // Идентификатор события
    Long id; //Идентификатор заявки
    Long requester; // Идентификатор пользователя, отправившего заявку
    String status; // example: PENDING Статус заявки
}
