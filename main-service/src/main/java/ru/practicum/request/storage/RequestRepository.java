package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEvent(Event event);

    List<Request> findAllByIdIsIn(List<Long> ids);

    List<Request> findAllByRequesterIs(User requester);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    long countRequestByEventAndStatus(Event event, RequestStatus status);

    List<Request> findAllByEventInAndStatus(List<Event> events, RequestStatus status);
}
