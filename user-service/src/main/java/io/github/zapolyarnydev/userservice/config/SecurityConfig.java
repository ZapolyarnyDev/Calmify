package io.github.zapolyarnydev.userservice.config;

import io.github.zapolyarnydev.commons.filter.HeaderFilterProvider;
import io.github.zapolyarnydev.userservice.filter.HeaderCheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var checkFiler = new HeaderCheckFilter(headerFilterProvider());

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .addFilterBefore(checkFiler, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private HeaderFilterProvider headerFilterProvider() {
        return HeaderFilterProvider.builder()
                .forEndpoints(
                        List.of("/v0/user"),
                        List.of("PATCH")
                )
                .requireHeaders(
                        List.of("Calmify-User-Email")
                )
                .build();
    }
}
