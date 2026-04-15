package com.agmdesarrollos.geopix.security;

import com.agmdesarrollos.geopix.logger.AuditLogService;
import com.agmdesarrollos.geopix.logger.jpa.AuditLogEntity.AuditAction;
import com.agmdesarrollos.geopix.security.dto.CreateUserRequest;
import com.agmdesarrollos.geopix.security.dto.UpdateUserRequest;
import com.agmdesarrollos.geopix.security.dto.UserResponse;
import com.agmdesarrollos.geopix.security.jpa.JpaRoleRepository;
import com.agmdesarrollos.geopix.security.jpa.JpaUserRepository;
import com.agmdesarrollos.geopix.security.jpa.RoleEntity;
import com.agmdesarrollos.geopix.security.jpa.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String ENTITY_NAME = "User";

    private final JpaUserRepository userRepository;
    private final JpaRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public UserService(JpaUserRepository userRepository,
                       JpaRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    // -------------------------------------------------------------------------
    // List
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<UserResponse> listActiveUsers() {
        return userRepository.findAllActive().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Get by ID
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        UserEntity user = userRepository.findActiveById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));
        return UserResponse.fromEntity(user);
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Validar unicidad
        if (userRepository.existsActiveByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre: " + request.getUsername());
        }
        if (userRepository.existsActiveByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.getEmail());
        }

        // Resolver roles
        Set<RoleEntity> roles = resolveRoles(request.getRoles());

        // Crear entidad
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setRoles(roles);

        UserEntity saved = userRepository.save(user);

        // Audit log
        String newValues = buildUserSnapshot(saved);
        auditLogService.log(
                ENTITY_NAME,
                saved.getId().toString(),
                AuditAction.CREATE,
                "Usuario creado: " + saved.getUsername(),
                null,
                newValues
        );

        log.info("Usuario creado: {} ({})", saved.getUsername(), saved.getId());
        return UserResponse.fromEntity(saved);
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        UserEntity user = userRepository.findActiveById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        // Snapshot previo
        String previousValues = buildUserSnapshot(user);
        List<String> changes = new ArrayList<>();

        // Username
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsActiveByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Ya existe un usuario con el nombre: " + request.getUsername());
            }
            changes.add("username: " + user.getUsername() + " -> " + request.getUsername());
            user.setUsername(request.getUsername());
        }

        // Email
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsActiveByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.getEmail());
            }
            changes.add("email: " + user.getEmail() + " -> " + request.getEmail());
            user.setEmail(request.getEmail());
        }

        // Password
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            changes.add("password: [cambiada]");
        }

        // Enabled
        if (request.getEnabled() != null && request.getEnabled() != user.isEnabled()) {
            changes.add("enabled: " + user.isEnabled() + " -> " + request.getEnabled());
            user.setEnabled(request.getEnabled());
        }

        // Roles
        if (request.getRoles() != null) {
            Set<String> currentRoleNames = user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());
            if (!currentRoleNames.equals(request.getRoles())) {
                Set<RoleEntity> newRoles = resolveRoles(request.getRoles());
                changes.add("roles: " + currentRoleNames + " -> " + request.getRoles());
                user.setRoles(newRoles);
            }
        }

        if (changes.isEmpty()) {
            log.info("No se detectaron cambios para el usuario: {} ({})", user.getUsername(), id);
            return UserResponse.fromEntity(user);
        }

        UserEntity saved = userRepository.save(user);

        // Audit log
        String newValues = buildUserSnapshot(saved);
        String description = "Usuario actualizado: " + String.join(", ", changes);
        auditLogService.log(
                ENTITY_NAME,
                saved.getId().toString(),
                AuditAction.UPDATE,
                description,
                previousValues,
                newValues
        );

        log.info("Usuario actualizado: {} ({}) -- {}", saved.getUsername(), id, description);
        return UserResponse.fromEntity(saved);
    }

    // -------------------------------------------------------------------------
    // Soft Delete
    // -------------------------------------------------------------------------

    @Transactional
    public void softDelete(UUID id) {
        UserEntity user = userRepository.findActiveById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        // No permitir que el admin se elimine a si mismo
        String currentUsername = getCurrentUsername();
        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("No puedes eliminar tu propio usuario");
        }

        String previousValues = buildUserSnapshot(user);

        user.setDeletedAt(Instant.now());
        user.setDeletedBy(currentUsername);
        userRepository.save(user);

        auditLogService.log(
                ENTITY_NAME,
                id.toString(),
                AuditAction.DELETE,
                "Usuario eliminado (soft delete): " + user.getUsername(),
                previousValues,
                null
        );

        log.info("Usuario eliminado (soft): {} ({}) por {}", user.getUsername(), id, currentUsername);
    }

    // -------------------------------------------------------------------------
    // Restore
    // -------------------------------------------------------------------------

    @Transactional
    public UserResponse restoreUser(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        if (!user.isDeleted()) {
            throw new IllegalArgumentException("El usuario no esta eliminado");
        }

        user.setDeletedAt(null);
        user.setDeletedBy(null);
        UserEntity saved = userRepository.save(user);

        String newValues = buildUserSnapshot(saved);
        auditLogService.log(
                ENTITY_NAME,
                id.toString(),
                AuditAction.RESTORE,
                "Usuario restaurado: " + user.getUsername(),
                null,
                newValues
        );

        log.info("Usuario restaurado: {} ({})", user.getUsername(), id);
        return UserResponse.fromEntity(saved);
    }

    // =========================================================================
    // Internal helpers
    // =========================================================================

    private Set<RoleEntity> resolveRoles(Set<String> roleNames) {
        Set<RoleEntity> roles = new HashSet<>();
        for (String roleName : roleNames) {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        return roles;
    }

    /**
     * Construye un snapshot JSON-like del usuario para el audit log.
     * Nunca incluye el password hash.
     */
    private String buildUserSnapshot(UserEntity user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return String.format(
                "{\"id\":\"%s\",\"username\":\"%s\",\"email\":\"%s\",\"enabled\":%s,\"roles\":%s,\"createdAt\":\"%s\",\"deletedAt\":%s,\"deletedBy\":%s}",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                roleNames,
                user.getCreatedAt(),
                user.getDeletedAt() != null ? "\"" + user.getDeletedAt() + "\"" : "null",
                user.getDeletedBy() != null ? "\"" + user.getDeletedBy() + "\"" : "null"
        );
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
