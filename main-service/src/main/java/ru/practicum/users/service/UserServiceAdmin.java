package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserServiceAdmin {
    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size);

    void deleteUserById(Long userId);
}
