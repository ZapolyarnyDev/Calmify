package io.github.zapolyarnydev.authservice.api.v0.advice;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> illegalArgument(IllegalArgumentException e) {
        var response = ApiResponse.fail(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> expiredToken(ExpiredJwtException ignored) {
        var response = ApiResponse.fail("Token expired");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<?>> malformedToken(MalformedJwtException ignored) {
        var response = ApiResponse.fail("Malformed token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
