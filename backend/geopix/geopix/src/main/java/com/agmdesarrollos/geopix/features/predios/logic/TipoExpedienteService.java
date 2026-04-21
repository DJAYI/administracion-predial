package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.config.GenericSpecification;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PaginatedResponse;
import com.agmdesarrollos.geopix.features.predios.http.dtos.ExpedientRequest;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PropertyTypeResponse;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoExpediente;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTipoExpedienteRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoExpedienteService {

    private final JpaTipoExpedienteRepository repository;

    public PaginatedResponse<PropertyTypeResponse> findAll(Pageable pageable, boolean includeDeletes) {
        Page<TipoExpediente> page = repository.findAll(
                GenericSpecification.filterDeleted(includeDeletes),
                pageable
        );
        
        return PaginatedResponse.<PropertyTypeResponse>builder()
                .content(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public List<PropertyTypeResponse> findAllSimple() {
        return repository.findAll(GenericSpecification.filterDeleted(false)).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PropertyTypeResponse findById(Long id) {
        return mapToResponse(getEntityById(id));
    }

    private TipoExpediente getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expedient not found with id: " + id));
    }

    public PropertyTypeResponse create(ExpedientRequest request) {
        TipoExpediente entity = new TipoExpediente();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setDeleted(false);
        return mapToResponse(repository.save(entity));
    }

    public PropertyTypeResponse update(Long id, ExpedientRequest request) {
        TipoExpediente entity = getEntityById(id);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return mapToResponse(repository.save(entity));
    }

    public void delete(Long id) {
        TipoExpediente entity = getEntityById(id);
        repository.delete(entity);
    }

    private PropertyTypeResponse mapToResponse(TipoExpediente entity) {
        return PropertyTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
