package io.github.zapolyarnydev.authservice.api.v1.advice;

import io.github.zapolyarnydev.authservice.api.common.ApiResponse;
import io.github.zapolyarnydev.authservice.api.common.ApiStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> illegalArgument(IllegalArgumentException e) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, e.getMessage(), null);
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> expiredToken(ExpiredJwtException ignored) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, "Token expired", null);
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<?>> malformedToken(MalformedJwtException ignored) {
        var response = new ApiResponse<>(ApiStatus.FAILURE, "Malformed token", null);
        return ResponseEntity.status(400).body(response);
    }
}
