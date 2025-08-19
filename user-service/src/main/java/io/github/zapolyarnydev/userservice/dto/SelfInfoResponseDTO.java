package io.github.zapolyarnydev.userservice.dto;

public record SelfInfoResponseDTO(
        String displayName,
        String handle,
        String description) {
}
