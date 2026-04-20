package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoTitularidad;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoTitularidadRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoTitularidadService {

    private final JpaTipoTitularidadRepository repository;

    public List<TipoTitularidad> findAll() {
        return repository.findAll();
    }

    public TipoTitularidad findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTitularidad not found with id: " + id));
    }

    public TipoTitularidad create(TipoTitularidad entity) {
        return repository.save(entity);
    }

    public TipoTitularidad update(Long id, TipoTitularidad details) {
        TipoTitularidad entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoTitularidad entity = findById(id);
        repository.delete(entity);
    }
}
