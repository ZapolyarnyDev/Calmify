package io.github.zapolyarnydev.userservice.api.v0.advice;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.commons.exception.UserNotFoundException;
import io.github.zapolyarnydev.userservice.exception.HandleAlreadyTakenException;
import io.github.zapolyarnydev.userservice.exception.IllegalHandleSizeException;
import io.github.zapolyarnydev.userservice.exception.InvalidHandleCharactersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler({
            HandleAlreadyTakenException.class,
            IllegalHandleSizeException.class,
            InvalidHandleCharactersException.class
    })
    public ResponseEntity<ApiResponse<?>> handleConflict(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(e.getMessage()));
    }
}