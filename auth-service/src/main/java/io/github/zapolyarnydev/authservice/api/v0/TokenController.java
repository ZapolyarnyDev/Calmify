package io.github.zapolyarnydev.authservice.api.v0;

import io.github.zapolyarnydev.authservice.api.common.ApiResponse;
import io.github.zapolyarnydev.authservice.api.common.ApiStatus;
import io.github.zapolyarnydev.authservice.service.RefreshTokenService;
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

        ApiResponse<String> apiResponse = new ApiResponse<>(
                ApiStatus.SUCCESS,
                "token has been updated",
                accessToken
        );

        return ResponseEntity.ok(apiResponse);
    }
}
