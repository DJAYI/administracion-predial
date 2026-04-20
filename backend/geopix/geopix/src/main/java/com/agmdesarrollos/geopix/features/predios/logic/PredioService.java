package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.http.dtos.PredioFullRequest;
import com.agmdesarrollos.geopix.features.predios.http.dtos.PredioResponse;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaPredioRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredioService {

    private final JpaPredioRepository repository;
    private final DatosBasicosPredioService basicService;
    private final DatosFisicosPredioService physicalService;
    private final TitularidadPredioService titularityService;

    @Transactional
    public PredioResponse saveFullPredio(PredioFullRequest request, Long id) {
        Predio predio;

        if (id == null) {
            // Creation: basicData required to start
            if (request.basicData() == null) {
                throw new IllegalArgumentException("Basic data is required for new property creation");
            }
            predio = basicService.addBasicData(request.basicData());
        } else {
            // Update: if basicData null, fetch existing
            if (request.basicData() != null) {
                predio = basicService.updateBasicData(id, request.basicData());
            } else {
                predio = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + id));
            }
        }

        // Apply Physical Data if present
        if (request.physicalData() != null) {
            predio = physicalService.updatePhysicalData(predio.getId(), request.physicalData());
        }

        // Apply Titularity if present
        if (request.titularity() != null) {
            titularityService.updateTitularity(predio.getId(), request.titularity());
            // Refresh owners list in memory
            predio = repository.findById(predio.getId()).get();
        }

        return PredioResponse.fromEntity(predio);
    }

    public List<PredioResponse> findAll() {
        return repository.findAll().stream()
                .map(PredioResponse::fromEntity)
                .toList();
    }

    public PredioResponse findById(Long id) {
        Predio predio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + id));
        return PredioResponse.fromEntity(predio);
    }

    public void delete(Long id) {
        Predio predio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + id));
        repository.delete(predio);
    }
}
