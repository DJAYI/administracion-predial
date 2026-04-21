package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.http.dtos.PaginatedResponse;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PropertyConditionRequest;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PropertyTypeResponse;
import com.agmdesarrollos.geopix.features.predios.logic.TipoCondicionPredioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/condiciones-predio")
@RequiredArgsConstructor
public class TipoCondicionPredioController {

    private final TipoCondicionPredioService service;

    @GetMapping
    public ResponseEntity<PaginatedResponse<PropertyTypeResponse>> findAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeDeletes
    ) {
        return ResponseEntity.ok(service.findAll(pageable, includeDeletes));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PropertyTypeResponse>> findAllSimple() {
        return ResponseEntity.ok(service.findAllSimple());
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<PropertyTypeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<PropertyTypeResponse> create(@RequestBody PropertyConditionRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<PropertyTypeResponse> update(@PathVariable Long id, @RequestBody PropertyConditionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTOR_INTEGRAL')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
