package org.example.liquorice.config.security;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtLoggingFilter.class);
    private final JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        extractToken(request).ifPresent(token -> {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                Instant expiration = jwt.getExpiresAt();
                if (expiration != null) {
                    long secondsLeft = Instant.now().until(expiration, ChronoUnit.SECONDS);
                    if (secondsLeft < 0) {
                        log.warn("JWT token is expired by {} seconds", Math.abs(secondsLeft));
                    } else {
                        log.info("JWT token remaining lifetime: {} seconds", secondsLeft);
                    }
                }
            } catch (Exception e) {
                log.debug("JWT logging: {}", e.getMessage());
            }
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }
}