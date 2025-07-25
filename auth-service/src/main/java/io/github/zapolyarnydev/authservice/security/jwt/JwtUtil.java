package io.github.zapolyarnydev.authservice.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .id(UUID.randomUUID().toString())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(jwtProperties.getAccessTokenExpiration())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    /**
     * Метод обновления токена доступа (access token) при помощи токена обновления (refresh token)
     *
     * @param refreshToken токен из cookie-файлов
     * @return новый токена доступа (access token)
     * */
    public String refreshToken(String refreshToken)
            throws IllegalArgumentException, ExpiredJwtException, MalformedJwtException {
        var claims = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        Object type = claims.get("type");
        if (type == null || !type.equals("refresh")) {
            throw new IllegalArgumentException("Invalid token type");
        }

        var subject = claims.getSubject();
        return generateAccessToken(subject);
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .id(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(jwtProperties.getRefreshTokenExpiration())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);

    }
}
