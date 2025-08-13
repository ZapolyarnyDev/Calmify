package io.github.zapolyarnydev.authservice.api.v0;

import io.github.zapolyarnydev.authservice.service.RefreshTokenService;
import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.commons.api.ApiStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(
            @RequestParam("refreshToken") @NotBlank(message = "refresh token cannot be null") String refreshToken) {
        String accessToken = tokenService.refreshToken(refreshToken);

        var apiResponse = ApiResponse.success("Token has been updated", accessToken);

        return ResponseEntity.ok(apiResponse);
    }
}
