package ru.practicum.exception;

public class CustomValidationDateException extends RuntimeException {
    public CustomValidationDateException(String message) {
        super(message);
    }
}
