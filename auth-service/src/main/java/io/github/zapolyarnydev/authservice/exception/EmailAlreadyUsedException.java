package io.github.zapolyarnydev.authservice.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("Email is already used: " + email);
    }
}
