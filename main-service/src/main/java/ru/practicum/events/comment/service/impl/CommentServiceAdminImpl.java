package ru.practicum.events.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.comment.dto.CommentDto;
import ru.practicum.events.comment.dto.CommentStateDto;
import ru.practicum.events.comment.dto.InputCommentDto;
import ru.practicum.events.comment.dto.UpdateCommentAdminDto;
import ru.practicum.events.comment.mapper.CommentMapper;
import ru.practicum.events.comment.model.Comment;
import ru.practicum.events.comment.service.CommentServiceAdmin;
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
public class CommentServiceAdminImpl implements CommentServiceAdmin {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public CommentDto createComment(InputCommentDto inputCommentDto) {
        Event event = getEventById(inputCommentDto.getEventId());
        User admin = getUserById(inputCommentDto.getUserId());
        Comment comment = CommentMapper.createComment(inputCommentDto, admin, event);
        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long commentId, UpdateCommentAdminDto updateComment) {
        Event event = getEventById(updateComment.getEventId());
        if (!userRepository.existsById(updateComment.getUserId())) {
            throw new ResourceNotFoundException("Пользователь c id = " + updateComment.getUserId() + " не найден");
        }
        Comment comment = getCommentByIdInRepository(commentId);
        CommentUtil.checkCommentOnEvent(comment, event);
        if (updateComment.getText() != null && !updateComment.getText().isBlank()) {
            comment.setText(updateComment.getText());
        }
        if (updateComment.getCommentStateDto() != null) {
            addCommentStatusAdmin(updateComment, comment);
        }
        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return CommentMapper.commentToCommentDto(getCommentByIdInRepository(commentId));
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        Event event = getEventById(eventId);
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findByEvent(event, pageable);
        return comments.stream().map(CommentMapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getCommentByIdInRepository(commentId);
        commentRepository.delete(comment);
    }

    private void addCommentStatusAdmin(UpdateCommentAdminDto updateComment, Comment comment) {
        if (updateComment.getCommentStateDto().equals(CommentStateDto.CANCELED)) {
            comment.setState(CommentMapper.toCommentState(updateComment.getCommentStateDto()));
            return;
        }
        if (updateComment.getCommentStateDto().equals(CommentStateDto.PUBLISHED)) {
            comment.setState(CommentMapper.toCommentState(updateComment.getCommentStateDto()));
            return;
        }
        if (updateComment.getCommentStateDto().equals(CommentStateDto.UPDATE)) {
            comment.setState(CommentMapper.toCommentState(updateComment.getCommentStateDto()));
        } else {
            throw new BadRequestException("Статус не соответствует модификатору доступа");
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
