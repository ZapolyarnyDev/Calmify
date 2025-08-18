package io.github.zapolyarnydev.userservice.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateUserRequestDTO(
        @Length(max = 32, message = "Maximum display name length is 32")
        String displayName,

        @Length(max = 20, message = "Maximum handle length is 20")
        String handle,

        @Length(max = 500, message = "Maximum description length is 500")
        String description
) {
}
