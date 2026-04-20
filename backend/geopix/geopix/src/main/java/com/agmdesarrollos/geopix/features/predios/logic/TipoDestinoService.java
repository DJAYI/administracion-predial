package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoDestino;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoDestinoRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoDestinoService {

    private final JpaTipoDestinoRepository repository;

    public List<TipoDestino> findAll() {
        return repository.findAll();
    }

    public TipoDestino findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDestino not found with id: " + id));
    }

    public TipoDestino create(TipoDestino entity) {
        return repository.save(entity);
    }

    public TipoDestino update(Long id, TipoDestino details) {
        TipoDestino entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoDestino entity = findById(id);
        repository.delete(entity);
    }
}
