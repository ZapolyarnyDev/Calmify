package io.github.zapolyarnydev.authservice.security;

import io.github.zapolyarnydev.authservice.filter.HeaderCheckFilter;
import io.github.zapolyarnydev.commons.filter.HeaderFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HeaderCheckFilter checkFilter) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .addFilterBefore(checkFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public HeaderFilterProvider headerFilterProvider() {
        return HeaderFilterProvider.builder()
                .forEndpoints(
                        List.of("/v0/token/refresh"),
                        List.of("POST")
                )
                .requireHeaders(
                        List.of("Calmify-User-Email")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
