package io.github.zapolyarnydev.userservice.exception;

public class HandleAlreadyTakenException extends RuntimeException {
    public HandleAlreadyTakenException(String handle) {
        super("Handle is already taken: " + handle);
    }
}
