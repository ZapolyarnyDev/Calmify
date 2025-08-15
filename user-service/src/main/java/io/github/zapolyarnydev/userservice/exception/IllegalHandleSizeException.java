package io.github.zapolyarnydev.userservice.exception;

public class IllegalHandleSizeException extends RuntimeException {
    public IllegalHandleSizeException(int minSize, int maxSize) {
        super(String.format("Handle size must be between %s and %s", minSize, maxSize));
    }
}
