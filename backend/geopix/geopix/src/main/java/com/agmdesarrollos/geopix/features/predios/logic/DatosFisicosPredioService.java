package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.http.dtos.DatosFisicosPredioRequest;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaPredioRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatosFisicosPredioService {

    private final JpaPredioRepository predioRepository;

    /**
     * Actualiza los datos físicos de un predio existente.
     * La lógica de cálculo de áreas totales se ejecuta automáticamente en la entidad (validateAndCalculate).
     */
    @Transactional
    public Predio updatePhysicalData(Long id, DatosFisicosPredioRequest request) {
        Predio predio = predioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + id));

        // Áreas VUR
        predio.setVurAreaHa(request.vurAreaHa());
        predio.setVurAreaM2(request.vurAreaM2());

        // Áreas Catastro
        predio.setCadastralAreaHa(request.cadastralAreaHa());
        predio.setCadastralAreaM2(request.cadastralAreaM2());

        // Áreas Escrituras
        predio.setDeedAreaHa(request.deedAreaHa());
        predio.setDeedAreaM2(request.deedAreaM2());

        // Áreas Medición
        predio.setMeasuredAreaHa(request.measuredAreaHa());
        predio.setMeasuredAreaM2(request.measuredAreaM2());

        // Linderos
        predio.setBoundariesAndDimensions(sanitize(request.boundariesAndDimensions()));

        return predioRepository.save(predio);
    }

    private String sanitize(String value) {
        return (value == null || value.trim().isEmpty()) ? Predio.DEFAULT_NO_INFO : value;
    }
}
