package ru.practicum.exception;

public class CustomConflictRequestException extends RuntimeException {
    public CustomConflictRequestException(String message) {
        super(message);
    }
}
