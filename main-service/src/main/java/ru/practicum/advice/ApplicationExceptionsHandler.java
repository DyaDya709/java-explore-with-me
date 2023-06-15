package ru.practicum.advice;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.ApiError;
import ru.practicum.exception.type.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionsHandler {

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleResourceNotFoundException(DataAccessException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Conflicted")
                .status("Conflicted")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("The required object was not found")
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConflictDeleteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictDeleteException(ConflictDeleteException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConflictRequestException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictRequestException(ConflictRequestException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConflictNameCategoryException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictNameCategoryException(ConflictNameCategoryException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConflictNameAndEmailException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictNameAndEmailException(ConflictNameAndEmailException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ForbiddenEventException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleForbiddenEventException(ForbiddenEventException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("FORBIDDEN")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ConflictEventPublicationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictEventPublicationException(ConflictEventPublicationException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("FORBIDDEN")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(ValidationDateException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleValidationDateException(ValidationDateException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .errors(errors)
                .message("Validation failed for some fields")
                .reason("The request contains invalid data")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MissingServletRequestParameterException e) {
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message("Validation failed for some fields")
                .reason("The request contains invalid data")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllExceptions(Throwable ex) {
        ApiError apiError = ApiError.builder()
                .errors(Collections.singletonList(ex.getMessage()))
                .message("Unexpected error occurred")
                .reason("An unexpected error occurred while processing the request")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return apiError;
    }
}
