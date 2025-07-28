package io.github.zapolyarnydev.unit.config;

import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class JwtTestsConfiguration {

    @Primary
    @Bean
    public AuthUserRepository userRepository() {
        return Mockito.mock(AuthUserRepository.class);
    }
}
