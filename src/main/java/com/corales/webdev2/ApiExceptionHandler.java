package com.corales.webdev2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PetConflictException.class)
    public ResponseEntity<ApiError> handleConflict(PetConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(PetRequestValidationException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(PetRequestValidationException exception) {
        return ResponseEntity.badRequest().body(new ApiError(exception.getMessage()));
    }
}
