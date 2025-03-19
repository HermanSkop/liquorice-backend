package org.example.liquorice.services;

import lombok.RequiredArgsConstructor;
import org.example.liquorice.config.AppConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    public void blacklistToken(String token, String reason) {
        redisTemplate.opsForValue().set(token, reason);
        redisTemplate.expire(
                token,
                jwtService.getTokenRemainingLifetimeMillis(token) + AppConfig.JWT_ACCESS_TOKEN_SECONDS_TIMEOUT_SKEW * 1000,
                TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}