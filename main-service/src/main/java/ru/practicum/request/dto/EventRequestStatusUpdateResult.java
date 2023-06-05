package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests; //подтвержденные запросы
    private List<ParticipationRequestDto> rejectedRequests; // отклоненные запросы
}
