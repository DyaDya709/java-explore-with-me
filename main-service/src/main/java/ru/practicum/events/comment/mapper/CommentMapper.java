package ru.practicum.events.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.CommentStateDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.model.Comment;
import ru.practicum.events.comment.model.CommentState;
import ru.practicum.events.event.mapper.EventMapper;
import ru.practicum.events.event.model.Event;
import ru.practicum.exception.type.BadRequestException;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class CommentMapper {
    public Comment createComment(InputCommentDto inputCommentDto, User author, Event event) {
        return Comment.builder()
                .text(inputCommentDto.getText())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .author(author)
                .event(event)
                .state(CommentState.PUBLISHED)
                .build();
    }

    public Comment updateComment(Long commentId, String text, User author, Event event) {
        return Comment.builder()
                .id(commentId)
                .text(text)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .author(author)
                .event(event)
                .state(CommentState.UPDATE)
                .build();
    }

    public CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .author(UserMapper.toShortDto(comment.getAuthor()))
                .event(EventMapper.toEventCommentDto(comment.getEvent()))
                .state(comment.getState().name())
                .build();

    }

    public CommentState toCommentState(CommentStateDto commentStateDto) {
        if (commentStateDto.name().equals(CommentState.UPDATE.name())) {
            return CommentState.UPDATE;
        }
        if (commentStateDto.name().equals(CommentState.PUBLISHED.name())) {
            return CommentState.PUBLISHED;
        }
        if (commentStateDto.name().equals(CommentState.CANCELED.name())) {
            return CommentState.CANCELED;
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
        }
    }
}
