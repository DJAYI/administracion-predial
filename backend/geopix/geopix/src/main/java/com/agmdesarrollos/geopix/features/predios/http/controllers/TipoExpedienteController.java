package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.logic.TipoExpedienteService;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoExpediente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-expediente")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TipoExpedienteController {

    private final TipoExpedienteService service;

    @GetMapping
    public ResponseEntity<List<TipoExpediente>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoExpediente> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoExpediente> create(@RequestBody TipoExpediente entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoExpediente> update(@PathVariable Long id, @RequestBody TipoExpediente entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
