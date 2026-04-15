package com.agmdesarrollos.geopix.security.dto;

import com.agmdesarrollos.geopix.security.jpa.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO de respuesta para datos de usuario. Nunca expone el password.
 */
@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private String deletedBy;

    public static UserResponse fromEntity(UserEntity user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        dto.setRoles(
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setDeletedAt(user.getDeletedAt());
        dto.setDeletedBy(user.getDeletedBy());
        return dto;
    }
}
