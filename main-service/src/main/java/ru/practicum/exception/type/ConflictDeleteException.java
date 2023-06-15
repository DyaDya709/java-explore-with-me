package ru.practicum.exception.type;

public class ConflictDeleteException extends RuntimeException {
    public ConflictDeleteException(String message) {
        super(message);
    }
}
