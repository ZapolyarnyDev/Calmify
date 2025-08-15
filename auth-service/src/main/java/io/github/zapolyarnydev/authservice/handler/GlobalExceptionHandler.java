package io.github.zapolyarnydev.authservice.handler;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.commons.exception.MissingCalmifyHeadersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingCalmifyHeadersException.class)
    public ResponseEntity<ApiResponse<?>> missingHeaders(MissingCalmifyHeadersException e) {
        var response = ApiResponse.fail(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
