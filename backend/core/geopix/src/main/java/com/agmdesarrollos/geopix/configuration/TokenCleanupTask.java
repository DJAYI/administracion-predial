package com.agmdesarrollos.geopix.configuration;

import com.agmdesarrollos.geopix.security.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically purges expired tokens from the database
 * to prevent unbounded table growth.
 */
@Component
public class TokenCleanupTask {

    private final TokenService tokenService;

    public TokenCleanupTask(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Runs every hour to delete tokens whose expiresAt has passed.
     */
    @Scheduled(fixedRateString = "${token.cleanup.interval-ms:3600000}")
    public void purgeExpiredTokens() {
        tokenService.purgeExpiredTokens();
    }
}
