package io.github.zapolyarnydev.gateway.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtKeyConfiguration {

    private final JwtProperties jwtProperties;

    @Bean
    public PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getPublicKeyBase64());
        var keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
