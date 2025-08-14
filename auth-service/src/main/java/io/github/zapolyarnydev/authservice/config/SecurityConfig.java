package io.github.zapolyarnydev.authservice.config;

import io.github.zapolyarnydev.commons.exception.MissingCalmifyHeadersException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/v0/auth/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(new HeaderCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    static class HeaderCheckFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {
            String path = request.getRequestURI();

            if ("/v0/token/refresh".equals(path)) {
                String emailHeader = request.getHeader("Calmify-User-Email");
                String roleHeader = request.getHeader("Calmify-User-Role");

                if (emailHeader == null || roleHeader == null) {
                    throw new MissingCalmifyHeadersException(
                            request.getRequestURI(),
                            List.of("Calmify-User-Email", "Calmify-User-Role")
                    );
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
