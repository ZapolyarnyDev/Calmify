package io.github.zapolyarnydev.authservice.security.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@ConfigurationProperties(prefix = "security.jwt")
@Validated
@Setter
@Getter
public class JwtProperties {

    @NotBlank(message = "Для запуска приложения необходимо указать приватный ключ для jwt")
    private String privateKeyBase64;

    @NotBlank(message = "Для запуска приложения необходимо указать публичный ключ для jwt")
    private String publicKeyBase64;

    @Min(1)
    private int accessTokenExpirationMinutes;
    @Min(0)
    private int refreshTokenExpirationDays;

    public Date getAccessTokenExpiration() {
        return Date.from(Instant.now().plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES));
    }

    public Date getRefreshTokenExpiration() {
        return Date.from(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
    }
}
