package io.github.zapolyarnydev.authservice.api.common;

public record ApiResponse<T>(ApiStatus status, String message, T data) {
}
