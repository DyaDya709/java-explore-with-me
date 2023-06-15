package ru.practicum.exception.type;

public class ConflictNameAndEmailException extends RuntimeException {
    public ConflictNameAndEmailException(String message) {
        super(message);
    }
}
