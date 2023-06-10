package ru.practicum.exception;

public class CustomConflictDeleteException extends RuntimeException {
    public CustomConflictDeleteException(String message) {
        super(message);
    }
}
