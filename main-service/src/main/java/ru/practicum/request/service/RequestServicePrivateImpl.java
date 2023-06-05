package ru.practicum.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RequestServicePrivateImpl implements RequestServicePrivate {
    @Override
    public List<ParticipationRequestDto> getAllRequestsUserById(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto addRequestEventById(Long userId, Long eventId, HttpServletRequest request) {
        return null;
    }

    @Override
    public ParticipationRequestDto updateRequestStatus(Long userId, Long requestId, HttpServletRequest request) {
        return null;
    }
}
