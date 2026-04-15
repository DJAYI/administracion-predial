package com.agmdesarrollos.geopix.predio.controller;

import com.agmdesarrollos.geopix.predio.dto.*;
import com.agmdesarrollos.geopix.predio.service.PredioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/predios")
public class PredioController {

    private final PredioService PredioService;

    public PredioController(PredioService PredioService) {
        this.PredioService = PredioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    public ResponseEntity<PredioResponse> crearDraft(
            @Valid @RequestBody CreatePredioRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String usuario = userDetails.getUsername();
        PredioResponse response = PredioService.crearDraft(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN', 'LECTOR')")
    public ResponseEntity<PredioResponse> obtenerDraft(@PathVariable String id) {
        PredioResponse response = PredioService.obtenerDraft(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    public ResponseEntity<PredioResponse> actualizarPaso(
            @PathVariable String id,
            @RequestParam Integer paso,
            @Valid @RequestBody UpdatePredioRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String usuario = userDetails.getUsername();
        PredioResponse response = PredioService.actualizarPaso(id, request, paso, usuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    public ResponseEntity<PredioResponse> completarPredio(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String usuario = userDetails.getUsername();
        PredioResponse response = PredioService.completarPredio(id, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-matricula")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN', 'LECTOR')")
    public ResponseEntity<Boolean> verificarMatricula(
            @RequestParam String matricula,
            @RequestParam(required = false) String excludeId) {
        
        boolean disponible;
        if (excludeId != null) {
            disponible = PredioService.verificarMatriculaDisponible(matricula, excludeId);
        } else {
            disponible = !PredioService.existeMatricula(matricula);
        }
        return ResponseEntity.ok(disponible);
    }
}