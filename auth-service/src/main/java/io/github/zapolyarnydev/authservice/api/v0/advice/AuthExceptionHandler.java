package io.github.zapolyarnydev.authservice.api.v0.advice;

import io.github.zapolyarnydev.authservice.api.common.ApiResponse;
import io.github.zapolyarnydev.authservice.api.common.ApiStatus;
import io.github.zapolyarnydev.authservice.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.authservice.exception.EmailNotFoundException;
import io.github.zapolyarnydev.authservice.exception.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiResponse<?>> emailUsed(EmailAlreadyUsedException e) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, e.getMessage(), null);
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> emailNotFound(EmailNotFoundException e) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, e.getMessage(), null);
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> invalidCredentials(InvalidCredentialsException e) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, e.getMessage(), null);
        return ResponseEntity.status(409).body(response);
    }

}
