package com.agmdesarrollos.geopix.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO para actualizar un usuario existente.
 * Todos los campos son opcionales: solo se actualizan los que vienen con valor.
 */
@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @Email(message = "El email debe tener un formato valido")
    private String email;

    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

    private Set<String> roles;

    private Boolean enabled;
}
