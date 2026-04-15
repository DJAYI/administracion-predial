package com.agmdesarrollos.geopix.security.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // ── Soft-delete aware queries ────────────────────────────────────────────

    /**
     * Find all active (non-deleted) users.
     */
    @Query("SELECT u FROM UserEntity u WHERE u.deletedAt IS NULL ORDER BY u.createdAt DESC")
    List<UserEntity> findAllActive();

    /**
     * Find an active user by ID (not soft-deleted).
     */
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<UserEntity> findActiveById(@Param("id") UUID id);

    /**
     * Find an active user by username (for login — exclude deleted users).
     */
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.deletedAt IS NULL")
    Optional<UserEntity> findActiveByUsername(@Param("username") String username);

    /**
     * Check if an active user with the given username exists (excludes deleted).
     */
    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.username = :username AND u.deletedAt IS NULL")
    boolean existsActiveByUsername(@Param("username") String username);

    /**
     * Check if an active user with the given email exists (excludes deleted).
     */
    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.email = :email AND u.deletedAt IS NULL")
    boolean existsActiveByEmail(@Param("email") String email);
}
