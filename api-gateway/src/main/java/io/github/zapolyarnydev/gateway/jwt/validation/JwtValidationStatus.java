package io.github.zapolyarnydev.gateway.jwt.validation;

public enum JwtValidationStatus {

    EXPIRED,
    WRONG_SIGNATURE,
    MALFORMED,
    VALIDATED
}
