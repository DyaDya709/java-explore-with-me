package ru.practicum.exception.type;

public class ForbiddenEventException extends RuntimeException {
    public ForbiddenEventException(String message) {
        super(message);
    }
}
