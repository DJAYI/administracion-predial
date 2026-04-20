package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.logic.TipoTitularidadService;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoTitularidad;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-titularidad")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TipoTitularidadController {

    private final TipoTitularidadService service;

    @GetMapping
    public ResponseEntity<List<TipoTitularidad>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTitularidad> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoTitularidad> create(@RequestBody TipoTitularidad entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoTitularidad> update(@PathVariable Long id, @RequestBody TipoTitularidad entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
