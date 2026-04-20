package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.logic.TipoServidumbreService;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoServidumbre;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-servidumbre")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TipoServidumbreController {

    private final TipoServidumbreService service;

    @GetMapping
    public ResponseEntity<List<TipoServidumbre>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServidumbre> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoServidumbre> create(@RequestBody TipoServidumbre entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoServidumbre> update(@PathVariable Long id, @RequestBody TipoServidumbre entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
