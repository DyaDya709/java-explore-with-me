package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserRequest;

import java.util.List;

public interface UserService {
    UserDto create(UserRequest userRequest);

    List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size);

    void deleteUserById(Long userId);
}
