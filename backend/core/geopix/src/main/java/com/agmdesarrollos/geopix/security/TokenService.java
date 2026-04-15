package com.agmdesarrollos.geopix.security;

import com.agmdesarrollos.geopix.security.jpa.JpaTokenRepository;
import com.agmdesarrollos.geopix.security.jpa.TokenEntity;
import com.agmdesarrollos.geopix.security.jpa.UserEntity;
import com.agmdesarrollos.geopix.utils.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final JpaTokenRepository tokenRepository;
    private final JwtService jwtService;

    public TokenService(JpaTokenRepository tokenRepository, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    /**
     * Generates a new access + refresh token pair, persists both in DB and returns them.
     * Before issuing, revokes any existing active tokens for the user.
     */
    @Transactional
    public TokenPair issueTokenPair(UserEntity user) {
        // Revoke previous active tokens for this user
        tokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        persistToken(accessToken, TokenEntity.TokenType.ACCESS, user);
        persistToken(refreshToken, TokenEntity.TokenType.REFRESH, user);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Rotates the refresh token: revokes the old pair and issues a new one.
     */
    @Transactional
    public TokenPair rotateTokens(UserEntity user) {
        // Revoke all existing tokens for this user before issuing new ones
        tokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        persistToken(accessToken, TokenEntity.TokenType.ACCESS, user);
        persistToken(refreshToken, TokenEntity.TokenType.REFRESH, user);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Revokes all active tokens for a user (logout).
     */
    @Transactional
    public void revokeAllUserTokens(UserEntity user) {
        int revoked = tokenRepository.revokeAllByUserId(user.getId());
        log.debug("Revoked {} tokens for user {}", revoked, user.getUsername());
    }

    /**
     * Checks if a raw JWT token is active (exists in DB and is not revoked).
     */
    @Transactional(readOnly = true)
    public boolean isTokenActive(String rawToken) {
        String hash = hashToken(rawToken);
        return tokenRepository.existsByTokenHashAndRevokedFalse(hash);
    }

    /**
     * Deletes all tokens that have expired before now (housekeeping).
     */
    @Transactional
    public int purgeExpiredTokens() {
        int deleted = tokenRepository.deleteAllExpiredBefore(Instant.now());
        if (deleted > 0) {
            log.info("Purged {} expired tokens", deleted);
        }
        return deleted;
    }

    // ── Internal ─────────────────────────────────────────────────────────────

    private void persistToken(String rawToken, TokenEntity.TokenType type, UserEntity user) {
        long expirationMs = (type == TokenEntity.TokenType.ACCESS)
                ? jwtService.getAccessTokenExpirationMs()
                : jwtService.getRefreshTokenExpirationMs();

        TokenEntity entity = new TokenEntity();
        entity.setTokenHash(hashToken(rawToken));
        entity.setTokenType(type);
        entity.setRevoked(false);
        entity.setIssuedAt(Instant.now());
        entity.setExpiresAt(Instant.now().plusMillis(expirationMs));
        entity.setUser(user);

        tokenRepository.save(entity);
    }

    /**
     * Hashes a raw JWT string with SHA-256 so we never store it in plain text.
     */
    static String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    // ── Value holder ─────────────────────────────────────────────────────────

    public record TokenPair(String accessToken, String refreshToken) {}
}
