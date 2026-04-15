package com.agmdesarrollos.geopix.security.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tokens", indexes = {
        @Index(name = "idx_token_hash", columnList = "tokenHash", unique = true),
        @Index(name = "idx_token_user_revoked", columnList = "user_id, revoked"),
        @Index(name = "idx_token_expires_at", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * SHA-256 hash of the JWT string. We never store the raw token
     * in the database -- only its hash for lookup and revocation.
     */
    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TokenType tokenType;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public enum TokenType {
        ACCESS,
        REFRESH
    }
}
