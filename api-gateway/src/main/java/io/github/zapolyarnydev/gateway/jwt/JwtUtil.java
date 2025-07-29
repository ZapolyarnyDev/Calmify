package io.github.zapolyarnydev.gateway.jwt;

import io.github.zapolyarnydev.gateway.jwt.validation.JwtValidationResult;
import io.github.zapolyarnydev.gateway.jwt.validation.JwtValidationStatus;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final PublicKey publicKey;

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);

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


}
