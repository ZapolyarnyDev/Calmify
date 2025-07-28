package io.github.zapolyarnydev.authservice.security.jwt.validation;

public record JwtValidationResult(JwtValidationStatus validationStatus, String message) {
}
