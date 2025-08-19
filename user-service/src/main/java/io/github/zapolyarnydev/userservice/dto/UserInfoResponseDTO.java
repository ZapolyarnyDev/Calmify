package io.github.zapolyarnydev.userservice.dto;

import java.time.Instant;

public record UserInfoResponseDTO(
        String displayName,
        String handle,
        String description,
        Instant lastSeenAt
) {
}
