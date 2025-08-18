package io.github.zapolyarnydev.commons.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("Couldn't find user with the email \"%s\"", email));
    }
}
