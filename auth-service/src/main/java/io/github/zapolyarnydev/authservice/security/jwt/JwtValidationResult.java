package io.github.zapolyarnydev.authservice.security.jwt;

public record JwtValidationResult(JwtValidationStatus validationStatus, String message) {
}
