package io.github.zapolyarnydev.gateway.filter;

import io.github.zapolyarnydev.gateway.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if(path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(!isAuthHeaderValid(authHeader)) {
            return error(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            Jws<Claims> claimsJws = jwtUtil.getClaims(token);

            Claims claims = claimsJws.getBody();
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            ServerHttpRequest mutated = mutateRequest(exchange, email, role);

            log.debug("JWT validated: {}, Role: {}", email, role);
            return chain.filter(exchange.mutate().request(mutated).build());

        } catch (Exception ex){
            return error(exchange, ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private ServerHttpRequest mutateRequest(ServerWebExchange exchange, String email, String role) {
        return exchange.getRequest().mutate()
                .header("Calmify-User-Email", email)
                .header("Calmify-User-Role", role)
                .build();
    }

    private boolean isAuthHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private Mono<Void> error(ServerWebExchange exchange, String message, HttpStatus code) {
        log.error("JWT authentication failed: {} | status: {} | path: {}", message, code.value(), exchange.getRequest().getURI().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(code);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
