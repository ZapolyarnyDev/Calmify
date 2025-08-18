package io.github.zapolyarnydev.userservice.filter;

import io.github.zapolyarnydev.commons.filter.HeaderFilterProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HeaderCheckFilter extends OncePerRequestFilter {

    private final HeaderFilterProvider filterProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        filterProvider.validatedHeaders(path, request::getHeader, request::getMethod);

        filterChain.doFilter(request, response);
    }
}
