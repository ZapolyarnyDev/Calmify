package io.github.zapolyarnydev.authservice.security.jwt;

public enum JwtValidationStatus {

    EXPIRED,
    WRONG_SIGNATURE,
    MALFORMED,
    VALIDATED
}
