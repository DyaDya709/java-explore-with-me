package ru.practicum.exception;

public class CustomConflictNameAndEmailException extends RuntimeException {
    public CustomConflictNameAndEmailException(String message) {
        super(message);
    }
}
