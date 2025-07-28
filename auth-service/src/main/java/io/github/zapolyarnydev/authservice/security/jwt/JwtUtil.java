package io.github.zapolyarnydev.authservice.security.jwt;

import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.security.jwt.claims.ClaimType;
import io.github.zapolyarnydev.authservice.security.jwt.claims.JwtClaim;
import io.github.zapolyarnydev.authservice.security.jwt.config.JwtProperties;
import io.github.zapolyarnydev.authservice.security.jwt.validation.JwtValidationResult;
import io.github.zapolyarnydev.authservice.security.jwt.validation.JwtValidationStatus;
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

    public String generateAccessToken(AuthUser authUser) {
        String email = authUser.getEmail();
        String role = authUser.getAuthRole().name();

        return Jwts.builder()
                .subject(email)
                .id(UUID.randomUUID().toString())
                .claim(JwtClaim.TYPE.getName(), ClaimType.ACCESS.getName())
                .claim(JwtClaim.ROLE.getName(), role)
                .issuedAt(new Date())
                .expiration(jwtProperties.getAccessTokenExpiration())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }



    public String generateRefreshToken(AuthUser authUser) {
        String email = authUser.getEmail();
        String role = authUser.getAuthRole().name();

        return Jwts.builder()
                .subject(email)
                .id(UUID.randomUUID().toString())
                .claim(JwtClaim.TYPE.getName(), ClaimType.REFRESH.getName())
                .claim(JwtClaim.ROLE.getName(), role)
                .issuedAt(new Date())
                .expiration(jwtProperties.getRefreshTokenExpiration())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public JwtValidationResult validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return new JwtValidationResult(JwtValidationStatus.VALIDATED, "Token has been successfully validated");
        } catch (ExpiredJwtException e) {
            return new JwtValidationResult(JwtValidationStatus.EXPIRED, "Token has expired");
        } catch (SignatureException e) {
            return new JwtValidationResult(JwtValidationStatus.WRONG_SIGNATURE, "Token has wrong signature");
        } catch (MalformedJwtException e) {
            return new JwtValidationResult(JwtValidationStatus.MALFORMED, "Тoken has wrong format");
        } catch (UnsupportedJwtException e) {
            return new JwtValidationResult(JwtValidationStatus.MALFORMED, "Token type doesn't support");
        } catch (IllegalArgumentException e) {
            return new JwtValidationResult(JwtValidationStatus.MALFORMED, "Wrong token argument");
        } catch (Exception e) {
            return new JwtValidationResult(JwtValidationStatus.MALFORMED, "Unknown error");
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
