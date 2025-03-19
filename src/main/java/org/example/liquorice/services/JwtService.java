package org.example.liquorice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.example.liquorice.config.security.JwtConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(Authentication authentication, long expiration) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("scope", scope)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration, ChronoUnit.MILLIS)))
                .signWith(getSigningKey(), AppConfig.JWT_SIGNATURE_ALGORITHM)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    public long getTokenRemainingLifetimeMillis(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

            Date expiration = claims.getExpiration();

            long remainingTime = expiration.getTime() - System.currentTimeMillis();

            return Math.max(0, remainingTime);
        } catch (Exception e) {
            return 0;
        }
    }
}
