package io.github.zapolyarnydev.authservice.dto.response;

public record JwtResponseDTO(String accessToken, String refreshToken) {
}
