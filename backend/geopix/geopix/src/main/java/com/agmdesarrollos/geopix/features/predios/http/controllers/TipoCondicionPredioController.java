package com.agmdesarrollos.geopix.features.predios.http.controllers;

import com.agmdesarrollos.geopix.features.predios.logic.TipoCondicionPredioService;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoCondicionPredio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-condicion-predio")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class TipoCondicionPredioController {

    private final TipoCondicionPredioService service;

    @GetMapping
    public ResponseEntity<List<TipoCondicionPredio>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCondicionPredio> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoCondicionPredio> create(@RequestBody TipoCondicionPredio entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCondicionPredio> update(@PathVariable Long id, @RequestBody TipoCondicionPredio entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
