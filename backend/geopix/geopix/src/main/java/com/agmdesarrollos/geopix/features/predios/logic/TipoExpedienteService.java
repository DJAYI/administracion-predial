package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoExpediente;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoExpedienteRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoExpedienteService {

    private final JpaTipoExpedienteRepository repository;

    public List<TipoExpediente> findAll() {
        return repository.findAll();
    }

    public TipoExpediente findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoExpediente not found with id: " + id));
    }

    public TipoExpediente create(TipoExpediente entity) {
        return repository.save(entity);
    }

    public TipoExpediente update(Long id, TipoExpediente details) {
        TipoExpediente entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoExpediente entity = findById(id);
        repository.delete(entity);
    }
}
