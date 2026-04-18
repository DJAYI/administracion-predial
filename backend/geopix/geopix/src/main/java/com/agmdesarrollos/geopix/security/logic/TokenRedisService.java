package com.agmdesarrollos.geopix.security.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private static final String ACCESS_TOKEN_PREFIX = "at:";
    private static final String REFRESH_TOKEN_PREFIX = "rt:";

    public void saveAccessToken(String username, String token) {
        redisTemplate.opsForValue().set(
                ACCESS_TOKEN_PREFIX + username,
                token,
                Duration.ofMillis(jwtExpiration)
        );
    }

    public void saveRefreshToken(String username, String token) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + username,
                token,
                Duration.ofMillis(refreshExpiration)
        );
    }

    public boolean isAccessTokenValid(String username, String token) {
        String storedToken = redisTemplate.opsForValue().get(ACCESS_TOKEN_PREFIX + username);
        return token.equals(storedToken);
    }

    public boolean isRefreshTokenValid(String username, String token) {
        String storedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
        return token.equals(storedToken);
    }

    public void deleteTokens(String username) {
        redisTemplate.delete(ACCESS_TOKEN_PREFIX + username);
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + username);
    }

    public void revokeAllUserTokens(String username) {
        deleteTokens(username);
    }
}
