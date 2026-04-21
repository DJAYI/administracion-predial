package com.agmdesarrollos.geopix.security.http.controllers;

import com.agmdesarrollos.geopix.security.http.dto.UserRequestDTO;
import com.agmdesarrollos.geopix.security.http.dto.UserResponseDTO;
import com.agmdesarrollos.geopix.security.logic.UserManagementService;
import com.agmdesarrollos.geopix.security.persistance.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

    private final UserManagementService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponseDTO.fromEntity(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO request) {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();
        return ResponseEntity.ok(UserResponseDTO.fromEntity(service.create(user, request.role())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO request) {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .enabled(request.enabled())
                .build();
        return ResponseEntity.ok(UserResponseDTO.fromEntity(service.update(id, user, request.role())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        service.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
