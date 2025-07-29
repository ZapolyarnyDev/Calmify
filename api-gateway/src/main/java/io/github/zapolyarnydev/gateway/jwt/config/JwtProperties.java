package io.github.zapolyarnydev.gateway.jwt.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "security.jwt")
@Validated
@Setter
@Getter
public class JwtProperties {

    @NotBlank(message = "Для запуска приложения необходимо указать публичный ключ для jwt")
    private String publicKeyBase64;
}
