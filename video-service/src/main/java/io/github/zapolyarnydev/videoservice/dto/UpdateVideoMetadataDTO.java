package io.github.zapolyarnydev.videoservice.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateVideoMetadataDTO(
        @Length(max = 50, message ="Video title can be up to 50 characters long")
        String title,
        @Length(max = 700, message ="Video description can be up to 700 characters long")
        String description

) {
}
