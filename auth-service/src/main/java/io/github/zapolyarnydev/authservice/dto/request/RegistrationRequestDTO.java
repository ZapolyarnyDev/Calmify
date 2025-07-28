package io.github.zapolyarnydev.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegistrationRequestDTO (
        @Email(message = "email format is invalid")
        @NotBlank(message = "email cannot be empty")
        String email,

        @NotBlank(message = "password cannot be empty")
        @Length(min = 8, max = 32, message = "password length must be between 8 and 32 characters")
        String password
) {
}
