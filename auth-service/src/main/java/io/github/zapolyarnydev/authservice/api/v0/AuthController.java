package io.github.zapolyarnydev.authservice.api.v0;

import io.github.zapolyarnydev.authservice.dto.request.LoginRequestDTO;
import io.github.zapolyarnydev.authservice.dto.request.RegistrationRequestDTO;
import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.security.jwt.JwtUtil;
import io.github.zapolyarnydev.authservice.service.AuthService;
import io.github.zapolyarnydev.commons.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegistrationRequestDTO registrationDTO) {
        var email = registrationDTO.email();
        var password = registrationDTO.password();

        AuthUser authUser = authService.register(email, password);

        String accessToken = jwtUtil.generateAccessToken(authUser);

        String refreshToken = jwtUtil.generateRefreshToken(authUser);
        ResponseCookie cookie = jwtUtil.tokenToCookie(refreshToken);

        var apiResponse = ApiResponse.success("Registration completed successfully", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        var email = loginRequestDTO.email();
        var password = loginRequestDTO.password();

        AuthUser authUser = authService.authenticate(email, password);

        String accessToken = jwtUtil.generateAccessToken(authUser);

        String refreshToken = jwtUtil.generateRefreshToken(authUser);
        ResponseCookie cookie = jwtUtil.tokenToCookie(refreshToken);

        var apiResponse = ApiResponse.success("Log in completed successfully", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(apiResponse);
    }
}
