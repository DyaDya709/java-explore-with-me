package ru.practicum.events.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.event.dto.EventFullDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.dto.NewEventDto;
import ru.practicum.events.event.dto.stateDto.ActionStateDto;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;
import ru.practicum.exception.type.BadRequestException;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.util.DateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class EventMapper {

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }


    public EventFullDto toDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(LocationMapper.toDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().name())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event newEventDtoToCreateEvent(NewEventDto newEventDto, User user, Category category, Long views,
                                                 Long confirmedRequests) {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(dateTime)
                .description(newEventDto.getDescription())
                .eventDate(DateFormatter.formatDate(newEventDto.getEventDate()))
                .initiator(user)
                .location(LocationMapper.toEntity(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
    }

    public EventState toEventState(ActionStateDto stateAction) {
        if (stateAction.equals(ActionStateDto.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        } else if (stateAction.equals(ActionStateDto.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        } else if (stateAction.equals(ActionStateDto.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        } else if (stateAction.equals(ActionStateDto.REJECT_EVENT)) {
            return EventState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }
}
