package io.github.zapolyarnydev.commons.exception;

import java.util.List;

public class MissingCalmifyHeadersException extends RuntimeException {
    public MissingCalmifyHeadersException(String path, String method, List<String> missingHeaders) {
        super("Missing headers " + missingHeaders + " for " + method + " " + path);
    }
}
