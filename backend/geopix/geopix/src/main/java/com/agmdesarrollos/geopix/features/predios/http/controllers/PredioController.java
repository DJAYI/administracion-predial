package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.http.dtos.PredioFullRequest;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PredioResponse;
import com.agmdesarrollos.geopix.features.predios.logic.PredioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/predios")
@RequiredArgsConstructor
public class PredioController {

    private final PredioService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<PredioResponse> create(@Valid @RequestBody PredioFullRequest request) {
        return ResponseEntity.ok(service.saveFullPredio(request, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<PredioResponse> update(@PathVariable Long id, @Valid @RequestBody PredioFullRequest request) {
        return ResponseEntity.ok(service.saveFullPredio(request, id));
    }

    @GetMapping
    public ResponseEntity<List<PredioResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
