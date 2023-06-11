package ru.practicum.events.request.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest { // Изменение статуса запроса на участие в событии текущего пользователя
    List<Long> requestIds; // Идентификаторы запросов на участие в событии текущего пользователя
    RequestStatusDto status;
}
