package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.logic.TipoSueloService;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoSuelo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-suelo")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TipoSueloController {

    private final TipoSueloService service;

    @GetMapping
    public ResponseEntity<List<TipoSuelo>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoSuelo> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoSuelo> create(@RequestBody TipoSuelo entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoSuelo> update(@PathVariable Long id, @RequestBody TipoSuelo entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
