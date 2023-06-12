package ru.practicum.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.type.BadRequestException;
import ru.practicum.exception.type.ConflictNameAndEmailException;
import ru.practicum.exception.type.ResourceNotFoundException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserServiceAdmin;
import ru.practicum.users.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserServiceAdmin {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.newUserRequestToUser(newUserRequest);
        try {
            return UserMapper.toDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictNameAndEmailException("Почта " + newUserRequest.getEmail() + " или имя пользователя " +
                    newUserRequest.getName() + " уже используется");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Запрос на добавление пользователя" + newUserRequest + " составлен не корректно ");
        }
    }

    @Override
    public List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = userRepository.findAllByIdIn(ids, pageable);
        } else {
            users = userRepository.findAllBy(pageable);
        }
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Пользователь c id = " + id + " не найден"));
    }
}
