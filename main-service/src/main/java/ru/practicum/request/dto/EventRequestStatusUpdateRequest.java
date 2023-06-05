package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds; // Идентификаторы запросов на участие в событии текущего пользователя
    RequestStatus status;
}
