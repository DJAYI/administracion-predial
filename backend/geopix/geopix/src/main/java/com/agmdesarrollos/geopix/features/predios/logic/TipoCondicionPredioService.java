package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoCondicionPredio;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoCondicionPredioRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCondicionPredioService {

    private final JpaTipoCondicionPredioRepository repository;

    public List<TipoCondicionPredio> findAll() {
        return repository.findAll();
    }

    public TipoCondicionPredio findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCondicionPredio not found with id: " + id));
    }

    public TipoCondicionPredio create(TipoCondicionPredio entity) {
        return repository.save(entity);
    }

    public TipoCondicionPredio update(Long id, TipoCondicionPredio details) {
        TipoCondicionPredio entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoCondicionPredio entity = findById(id);
        repository.delete(entity);
    }
}
