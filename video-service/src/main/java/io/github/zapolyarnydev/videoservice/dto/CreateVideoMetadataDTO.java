package io.github.zapolyarnydev.videoservice.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record CreateVideoMetadataDTO(
        @NotNull(message = "Video title cannot be null")
        @Length(max = 50, message ="Video title can be up to 50 characters long")
        String title,
        @Length(max = 700, message ="Video description can be up to 700 characters long")
        String description
) {
}
