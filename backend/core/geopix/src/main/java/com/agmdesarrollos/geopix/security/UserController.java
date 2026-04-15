package com.agmdesarrollos.geopix.security;

import com.agmdesarrollos.geopix.logger.AuditLogService;
import com.agmdesarrollos.geopix.logger.jpa.AuditLogEntity;
import com.agmdesarrollos.geopix.security.dto.CreateUserRequest;
import com.agmdesarrollos.geopix.security.dto.UpdateUserRequest;
import com.agmdesarrollos.geopix.security.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final AuditLogService auditLogService;

    public UserController(UserService userService, AuditLogService auditLogService) {
        this.userService = userService;
        this.auditLogService = auditLogService;
    }

    // -------------------------------------------------------------------------
    // List users
    // -------------------------------------------------------------------------

    /**
     * GET /api/users
     * Lista solo usuarios activos (no eliminados).
     * Query param ?includeDeleted=true para incluir los soft-deleted.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers(
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        List<UserResponse> users = includeDeleted
                ? userService.listAllUsers()
                : userService.listActiveUsers();
        return ResponseEntity.ok(users);
    }

    // -------------------------------------------------------------------------
    // Get by ID
    // -------------------------------------------------------------------------

    /**
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    /**
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    /**
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // -------------------------------------------------------------------------
    // Soft Delete
    // -------------------------------------------------------------------------

    /**
     * DELETE /api/users/{id}
     * Soft delete — marca el usuario como eliminado sin borrarlo fisicamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID id) {
        userService.softDelete(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }

    // -------------------------------------------------------------------------
    // Restore
    // -------------------------------------------------------------------------

    /**
     * POST /api/users/{id}/restore
     * Restaura un usuario previamente eliminado (soft delete).
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.restoreUser(id));
    }

    // -------------------------------------------------------------------------
    // Audit history for a specific user
    // -------------------------------------------------------------------------

    /**
     * GET /api/users/{id}/history
     * Retorna el historial de auditoria de un usuario especifico.
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<List<AuditLogEntity>> getUserHistory(@PathVariable UUID id) {
        List<AuditLogEntity> history = auditLogService.getEntityHistory("User", id.toString());
        return ResponseEntity.ok(history);
    }
}
