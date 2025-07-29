package io.github.zapolyarnydev.gateway.jwt.validation;


public record JwtValidationResult(JwtValidationStatus validationStatus, String message) {
}
