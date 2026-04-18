package com.agmdesarrollos.geopix.security.http.controllers;

import com.agmdesarrollos.geopix.security.http.dto.*;
import com.agmdesarrollos.geopix.security.http.services.AuthHttpService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthHttpService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
}
