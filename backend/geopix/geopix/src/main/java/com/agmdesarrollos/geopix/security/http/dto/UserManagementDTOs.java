package com.agmdesarrollos.geopix.security.http.dto;

import com.agmdesarrollos.geopix.security.persistance.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record UserRequestDTO(
    @NotBlank(message = "Username is mandatory")
    String username,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    String email,

    String password, // Optional on update

    @NotBlank(message = "Role is mandatory")
    String role,

    boolean enabled
) {
}

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
