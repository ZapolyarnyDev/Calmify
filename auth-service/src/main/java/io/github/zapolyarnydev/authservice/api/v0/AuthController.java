package io.github.zapolyarnydev.authservice.api.v0;

import io.github.zapolyarnydev.authservice.dto.request.LoginRequestDTO;
import io.github.zapolyarnydev.authservice.dto.request.RegistrationRequestDTO;
import io.github.zapolyarnydev.authservice.dto.response.JwtResponseDTO;
import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.security.jwt.JwtUtil;
import io.github.zapolyarnydev.authservice.service.AuthService;
import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.commons.api.ApiStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> register(@Valid @RequestBody RegistrationRequestDTO registrationDTO) {
        var email = registrationDTO.email();
        var password = registrationDTO.password();

        AuthUser authUser = authService.register(email, password);

        String accessToken = jwtUtil.generateAccessToken(authUser);
        String refreshToken = jwtUtil.generateRefreshToken(authUser);

        var responseDTO = new JwtResponseDTO(accessToken, refreshToken);

        var apiResponse = ApiResponse.success("Registration completed successfully", responseDTO);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        var email = loginRequestDTO.email();
        var password = loginRequestDTO.password();

        AuthUser authUser = authService.authenticate(email, password);

        String accessToken = jwtUtil.generateAccessToken(authUser);
        String refreshToken = jwtUtil.generateRefreshToken(authUser);

        var responseDTO = new JwtResponseDTO(accessToken, refreshToken);

        var apiResponse = ApiResponse.success("Log in completed successfully", responseDTO);

        return ResponseEntity.ok(apiResponse);
    }
}
