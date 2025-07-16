package com.backend.UniErrands.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. DTO Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Generic bad request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of(
            "error", ex.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    // 3. Custom Not Found Exceptions
    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class, ChatNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions(RuntimeException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of(
            "error", ex.getMessage()
        ), HttpStatus.NOT_FOUND); // changed from 400 to 404
    }

    // 4. Catch-All
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        logger.error("Unhandled Exception: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of(
            "error", "Internal Server Error",
            "details", ex.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
