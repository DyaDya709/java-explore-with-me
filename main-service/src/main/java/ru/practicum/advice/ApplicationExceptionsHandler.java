package ru.practicum.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;
import ru.practicum.exception.model.CustomError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionsHandler {
    @ExceptionHandler(CustomResourceNotFoundException.class)
    public ResponseEntity<CustomError> handleResourceNotFoundException(CustomResourceNotFoundException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("The required object was not found")
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<CustomError> handleBadRequestException(CustomBadRequestException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomConflictDeleteException.class)
    public ResponseEntity<CustomError> handleConflictDeleteException(CustomConflictDeleteException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomConflictRequestException.class)
    public ResponseEntity<CustomError> handleConflictRequestException(CustomConflictRequestException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomConflictNameCategoryException.class)
    public ResponseEntity<CustomError> handleConflictNameCategoryException(CustomConflictNameCategoryException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomConflictNameAndEmailException.class)
    public ResponseEntity<CustomError> handleConflictNameAndEmailException(CustomConflictNameAndEmailException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomForbiddenEventException.class)
    public ResponseEntity<CustomError> handleForbiddenEventException(CustomForbiddenEventException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status("FORBIDDEN")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomValidationDateException.class)
    public ResponseEntity<CustomError> handleValidationDateException(CustomValidationDateException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
        log.warn("Incorrectly made request: {}", e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        CustomError apiError = CustomError.builder()
                .errors(errors)
                .message("Validation failed for some fields")
                .reason("The request contains invalid data")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn("Validation failed for some fields: {}", e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomError> handleValidationException(MissingServletRequestParameterException e) {
        CustomError apiError = CustomError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message("Validation failed for some fields")
                .reason("The request contains invalid data")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        log.warn("Validation failed for some fields: {}", e.getMessage(), e);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Throwable.class)
//    public ResponseEntity<CustomError> handleAllExceptions(Throwable ex) {
//        CustomError apiError = CustomError.builder()
//                .errors(Collections.singletonList(ex.getMessage()))
//                .message("Unexpected error occurred")
//                .reason("An unexpected error occurred while processing the request")
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
//                .timestamp(LocalDateTime.now())
//                .build();
//        log.warn("Unexpected error occurred: {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
