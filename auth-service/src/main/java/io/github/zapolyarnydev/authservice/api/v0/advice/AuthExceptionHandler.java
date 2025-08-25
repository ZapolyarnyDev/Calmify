package io.github.zapolyarnydev.authservice.api.v0.advice;

import io.github.zapolyarnydev.authservice.exception.EmailNotFoundException;
import io.github.zapolyarnydev.authservice.exception.InvalidCredentialsException;
import io.github.zapolyarnydev.commons.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> emailNotFound(EmailNotFoundException e) {
        var response = ApiResponse.fail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            InvalidCredentialsException.class,
            EmailNotFoundException.class
    })
    public ResponseEntity<ApiResponse<?>> wrongCredentials(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(e.getMessage()));
    }

}
