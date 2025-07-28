package io.github.zapolyarnydev.authservice.security.jwt.validation;

public enum JwtValidationStatus {

    EXPIRED,
    WRONG_SIGNATURE,
    MALFORMED,
    VALIDATED
}
