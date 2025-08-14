package io.github.zapolyarnydev.commons.exception;

import java.util.Arrays;
import java.util.List;

public class MissingCalmifyHeadersException extends RuntimeException {

    public MissingCalmifyHeadersException(String target, List<String> headers) {
        super(String.format("Missing Calmify headers for %s: %s", target, String.join(", ", headers)));
    }
}
