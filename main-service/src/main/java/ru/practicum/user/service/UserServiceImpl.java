package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.CustomBadRequestException;
import ru.practicum.exception.CustomConflictNameAndEmailException;
import ru.practicum.exception.CustomResourceNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserRequest userRequest) {
        User user = UserMapper.userRequestToUser(userRequest);
        try {
            return UserMapper.userToDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new CustomConflictNameAndEmailException("Почта " + userRequest.getEmail() + " или имя пользователя " +
                    userRequest.getName() + " уже используется");
        } catch (IllegalArgumentException e) {
            throw new CustomBadRequestException("Запрос на добавление пользователя" + userRequest + " составлен не корректно ");
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
        return users.stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new CustomResourceNotFoundException("Пользователь c id = " + id + " не найден"));
    }
}
