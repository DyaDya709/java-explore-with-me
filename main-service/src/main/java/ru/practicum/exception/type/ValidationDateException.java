package ru.practicum.exception.type;

public class ValidationDateException extends RuntimeException {
    public ValidationDateException(String message) {
        super(message);
    }
}
