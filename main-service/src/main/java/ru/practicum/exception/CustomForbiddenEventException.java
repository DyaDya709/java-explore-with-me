package ru.practicum.exception;

public class CustomForbiddenEventException extends RuntimeException {
    public CustomForbiddenEventException(String message) {
        super(message);
    }
}
