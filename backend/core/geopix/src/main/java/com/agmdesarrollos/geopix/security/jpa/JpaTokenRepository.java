package com.agmdesarrollos.geopix.security.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTokenRepository extends JpaRepository<TokenEntity, UUID> {

    /**
     * Find a token by its SHA-256 hash (for validation during requests).
     */
    Optional<TokenEntity> findByTokenHash(String tokenHash);

    /**
     * Check if a non-revoked token with the given hash exists.
     */
    boolean existsByTokenHashAndRevokedFalse(String tokenHash);

    /**
     * Revoke all active tokens for a given user (used on logout).
     */
    @Modifying
    @Query("UPDATE TokenEntity t SET t.revoked = true WHERE t.user.id = :userId AND t.revoked = false")
    int revokeAllByUserId(@Param("userId") UUID userId);

    /**
     * Revoke all active tokens of a specific type for a given user (used on refresh rotation).
     */
    @Modifying
    @Query("UPDATE TokenEntity t SET t.revoked = true WHERE t.user.id = :userId AND t.tokenType = :tokenType AND t.revoked = false")
    int revokeAllByUserIdAndTokenType(@Param("userId") UUID userId, @Param("tokenType") TokenEntity.TokenType tokenType);

    /**
     * Delete expired tokens (housekeeping).
     */
    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.expiresAt < :now")
    int deleteAllExpiredBefore(@Param("now") Instant now);
}
