package ru.practicum.request.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "requests", schema = "public")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id; //Идентификатор заявки
    @Column(name = "created")
    private LocalDateTime created; // 2022-09-06T21:10:05.432 Дата и время создания заявки
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // Идентификатор пользователя, отправившего заявку
    @Enumerated(EnumType.STRING)
    private RequestStatus status; // example: PENDING Статус заявки
}
