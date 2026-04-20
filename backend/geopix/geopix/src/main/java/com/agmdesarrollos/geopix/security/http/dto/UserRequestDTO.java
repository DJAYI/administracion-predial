package com.agmdesarrollos.geopix.security.http.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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
