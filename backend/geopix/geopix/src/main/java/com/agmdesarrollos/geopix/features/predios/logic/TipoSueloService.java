package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoSuelo;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoSueloRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoSueloService {

    private final JpaTipoSueloRepository repository;

    public List<TipoSuelo> findAll() {
        return repository.findAll();
    }

    public TipoSuelo findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoSuelo not found with id: " + id));
    }

    public TipoSuelo create(TipoSuelo entity) {
        return repository.save(entity);
    }

    public TipoSuelo update(Long id, TipoSuelo details) {
        TipoSuelo entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoSuelo entity = findById(id);
        repository.delete(entity);
    }
}
