package io.github.zapolyarnydev.authservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("Couldn't find user with the email \"%s\"", email));
    }
}
