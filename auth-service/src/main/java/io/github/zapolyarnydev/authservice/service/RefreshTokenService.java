package io.github.zapolyarnydev.authservice.service;

import io.github.zapolyarnydev.authservice.exception.EmailNotFoundException;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import io.github.zapolyarnydev.authservice.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final AuthUserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String refreshToken(String refreshToken)
            throws IllegalArgumentException, ExpiredJwtException, MalformedJwtException {
        var claims = jwtUtil.getClaims(refreshToken).getBody();

        Object type = claims.get("type");
        if (type == null || !type.equals("refresh")) {
            throw new IllegalArgumentException("Invalid token type");
        }

        var email = claims.getSubject();

        var user = userRepository.findByEmail(email);

        return user.map(jwtUtil::generateAccessToken).orElseThrow(() -> new EmailNotFoundException(email));

    }
}
