package io.github.zapolyarnydev.userservice.exception;

public class InvalidHandleCharactersException extends RuntimeException {
    public InvalidHandleCharactersException() {
        super("Handle contains wrong characters");
    }
}
