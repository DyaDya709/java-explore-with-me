package ru.practicum.events.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.mapper.CommentMapper;
import ru.practicum.events.comment.model.Comment;
import ru.practicum.events.comment.model.CommentState;
import ru.practicum.events.comment.service.CommentServicePrivate;
import ru.practicum.events.comment.storage.CommentRepository;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.type.BadRequestException;
import ru.practicum.exception.type.ResourceNotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServicePrivateImpl implements CommentServicePrivate {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDto createComment(InputCommentDto inputCommentDto) {
        CommentUtil.moderateMessage(inputCommentDto.getText());
        Event event = getEventById(inputCommentDto.getEventId());
        User user = getUserById(inputCommentDto.getUserId());
        Comment comment = CommentMapper.createComment(inputCommentDto, user, event);
        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long commentId, InputCommentDto inputCommentDto) {
        CommentUtil.moderateMessage(inputCommentDto.getText());
        Comment comment = getCommentByIdInRepository(commentId);
        checkCommentStatus(comment);
        Event event = getEventById(inputCommentDto.getEventId());
        User user = getUserById(inputCommentDto.getUserId());
        CommentUtil.checkCommentOnEvent(comment, event);
        CommentUtil.checkCommentOnOwner(comment, user);
        Comment newComment = CommentMapper.updateComment(comment.getId(), inputCommentDto.getText(), user, event);
        return CommentMapper.commentToCommentDto(commentRepository.save(newComment));
    }

    @Override
    public CommentDto getCommentById(Long commentId, Long userId) {
        Comment comment = getCommentByIdInRepository(commentId);
        User user = getUserById(userId);
        CommentUtil.checkCommentOnOwner(comment, user);
        return CommentMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Long userId, Integer from, Integer size) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEventAndAuthor(event, user, pageable);
        return comments.stream().map(CommentMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId) {
        User user = getUserById(userId);
        Comment comment = getCommentByIdInRepository(commentId);
        CommentUtil.checkCommentOnOwner(comment, user);
        commentRepository.delete(comment);
    }

    private void checkCommentStatus(Comment comment) {
        if (comment.getState().equals(CommentState.CANCELED)) {
            throw new BadRequestException("Комментарий с id=" + comment.getId() + " ранее был отменен");
        }
    }

    private Event getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Событие c id = " + id + " не найдено"));

        return event;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id = " + id + " не найден"));
    }

    private Comment getCommentByIdInRepository(Long id) {
        return commentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Комментарий c id = " + id + " не найден"));
    }
}
