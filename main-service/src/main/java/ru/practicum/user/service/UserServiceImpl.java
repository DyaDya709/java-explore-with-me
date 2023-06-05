package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserRequest;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public UserDto create(UserRequest userRequest) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsersByIds(List<Long> ids, int from, int size) {
        return null;
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
