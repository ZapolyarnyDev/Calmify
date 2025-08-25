package io.github.zapolyarnydev.videoservice.dto;

import java.time.Instant;

public record VideoMetadataInfo(
        String tile,
        String description,
        Instant uploadedAt
) {
}
