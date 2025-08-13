package io.github.zapolyarnydev.commons.api;

public record ApiResponse<T>(ApiStatus status, String message, T data) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, message, data);
    }

    public static ApiResponse<?> fail(String message) {
        return new ApiResponse<>(ApiStatus.FAILURE, message, null);
    }

    public static <T> ApiResponse<T> fail(String message, T data) {
        return new ApiResponse<>(ApiStatus.FAILURE, message, data);
    }

}
