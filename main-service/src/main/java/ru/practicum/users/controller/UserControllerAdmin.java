package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserServiceAdmin;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    private final UserServiceAdmin userServiceAdmin;


    @GetMapping
    List<UserDto> getAllUsersByIds(@RequestParam(required = false, name = "ids") List<Long> ids,
                                   @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") Integer from,
                                   @Positive @RequestParam(defaultValue = "10", name = "size") Integer size) {
        return userServiceAdmin.getAllUsersByIds(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@Validated @RequestBody NewUserRequest newUserRequest) {
        return userServiceAdmin.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserById(@PathVariable Long userId) {
        userServiceAdmin.deleteUserById(userId);
    }
}
