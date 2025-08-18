package io.github.zapolyarnydev.commons.filter;

import java.util.List;

public record Endpoint(String path, List<String> methods) {
}
