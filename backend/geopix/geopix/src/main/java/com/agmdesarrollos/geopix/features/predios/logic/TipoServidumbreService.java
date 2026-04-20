package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoServidumbre;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoServidumbreRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoServidumbreService {

    private final JpaTipoServidumbreRepository repository;

    public List<TipoServidumbre> findAll() {
        return repository.findAll();
    }

    public TipoServidumbre findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoServidumbre not found with id: " + id));
    }

    public TipoServidumbre create(TipoServidumbre entity) {
        return repository.save(entity);
    }

    public TipoServidumbre update(Long id, TipoServidumbre details) {
        TipoServidumbre entity = findById(id);
        entity.setName(details.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        TipoServidumbre entity = findById(id);
        repository.delete(entity);
    }
}
