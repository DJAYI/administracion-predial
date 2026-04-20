package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.http.dtos.DatosBasicosPredioRequest;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoCondicionPredio;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.*;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.DepartamentoRepository;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.MunicipioRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatosBasicosPredioService {
    private final JpaPredioRepository predioRepository;
    private final MunicipioRepository municipioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final JpaTipoCondicionPredioRepository tipoCondicionPredioRepository;
    private final JpaTipoServidumbreRepository tipoServidumbreRepository;
    private final JpaTipoSueloRepository tipoSueloRepository;
    private final JpaTipoDestinoRepository tipoDestinoRepository;
    private final JpaTipoTitularidadRepository tipoTitularidadRepository;
    private final JpaTipoExpedienteRepository tipoExpedienteRepository;

    @Transactional
    public Predio addBasicData(DatosBasicosPredioRequest request) {
        Predio newPredio = new Predio();
        mapRequestToPredio(request, newPredio);
        return predioRepository.save(newPredio);
    }

    @Transactional
    public Predio updateBasicData(Long id, DatosBasicosPredioRequest request) {
        Predio existingPredio = predioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + id));
        mapRequestToPredio(request, existingPredio);
        return predioRepository.save(existingPredio);
    }

    private void mapRequestToPredio(DatosBasicosPredioRequest request, Predio predio) {
        predio.setRealEstateRegistration(request.realEstateRegistration());
        predio.setNationalPropertyNumber(sanitize(request.nationalPropertyNumber()));
        predio.setCadastralReference(sanitize(request.cadastralReference()));
        predio.setNationalPropertyNumberGeo(sanitize(request.nationalPropertyNumberGeo()));
        predio.setHasEasement(request.hasEasement());
        predio.setAddress(request.address());
        predio.setAlias(request.alias());
        predio.setEnvironmentalImpact(request.environmentalImpact());
        predio.setObservations(sanitize(request.observations()));

        predio.setDepartment(departamentoRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));

        predio.setMunicipality(municipioRepository.findById(request.municipalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Municipality not found")));

        predio.setSoilType(tipoSueloRepository.findById(request.soilTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Soil Type not found")));

        predio.setDestinationType(tipoDestinoRepository.findById(request.destinationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Type not found")));

        predio.setOwnershipType(tipoTitularidadRepository.findById(request.ownershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ownership Type not found")));

        predio.setRecordType(tipoExpedienteRepository.findById(request.recordTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Record Type not found")));

        if ("SI".equalsIgnoreCase(request.hasEasement())) {
            if (request.easementTypeId() == null) {
                throw new IllegalArgumentException("Easement Type is required when hasEasement is SI");
            }
            predio.setEasementType(tipoServidumbreRepository.findById(request.easementTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Easement Type not found")));
        } else {
            predio.setEasementType(null);
        }

        List<TipoCondicionPredio> conditions = tipoCondicionPredioRepository.findAllById(request.propertyConditions());
        if (conditions.size() != request.propertyConditions().size()) {
            throw new ResourceNotFoundException("One or more Property Conditions not found");
        }
        predio.setPropertyConditions(conditions);
    }

    private String sanitize(String value) {
        return (value == null || value.trim().isEmpty()) ? Predio.DEFAULT_NO_INFO : value;
    }
}
