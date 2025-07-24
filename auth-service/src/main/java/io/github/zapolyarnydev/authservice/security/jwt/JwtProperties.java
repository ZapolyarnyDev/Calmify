package io.github.zapolyarnydev.authservice.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "secrets.jwt")
@Validated
@Getter
@Setter
public class JwtProperties {
    private String privateKeyBase64;
    private String publicKeyBase64;
}
