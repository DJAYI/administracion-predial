package com.agmdesarrollos.geopix.security.http.dto;

import com.agmdesarrollos.geopix.security.persistance.entities.User;

import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String email,
    String role,
    boolean enabled
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().getName(),
            user.isEnabled()
        );
    }
}
