package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.http.dtos.TitularesPredioRequest;
import com.agmdesarrollos.geopix.features.predios.http.dtos.TitularRequest;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Titular;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaPredioRepository;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaTitularRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TitularidadPredioService {

    private final JpaPredioRepository predioRepository;
    private final JpaTitularRepository titularRepository;

    @Transactional
    public List<Titular> updateTitularity(Long predioId, TitularesPredioRequest request) {
        Predio predio = predioRepository.findById(predioId)
                .orElseThrow(() -> new ResourceNotFoundException("Predio not found with id: " + predioId));

        validateTitularityRules(request);

        // Delete existing owners to replace with new list (Bulk Update pattern)
        if (predio.getOwners() != null && !predio.getOwners().isEmpty()) {
            titularRepository.deleteAll(predio.getOwners());
            predio.getOwners().clear();
        }

        List<Titular> newOwners = new ArrayList<>();
        for (TitularRequest ownerDto : request.owners()) {
            Titular titular = mapDtoToEntity(ownerDto, predio);
            newOwners.add(titular);
        }

        return titularRepository.saveAll(newOwners);
    }

    private void validateTitularityRules(TitularesPredioRequest request) {
        double totalPercent = 0.0;
        Set<String> uniqueDocuments = new HashSet<>();

        for (TitularRequest dto : request.owners()) {
            // 1. Unique ID check within the same property
            String docKey = dto.documentType() + "_" + dto.documentNumber();
            if (!uniqueDocuments.add(docKey)) {
                throw new IllegalArgumentException("Duplicate owner document: " + docKey);
            }

            // 2. Percentage sum
            totalPercent += dto.rightPercent();

            // 3. Natural vs Juridical Logic
            if ("NIT".equalsIgnoreCase(dto.documentType())) {
                if (dto.socialReason() == null || dto.socialReason().isBlank()) {
                    throw new IllegalArgumentException("Social Reason is mandatory for NIT");
                }
            } else {
                if (dto.firstName() == null || dto.firstName().isBlank() || 
                    dto.firstSurname() == null || dto.firstSurname().isBlank()) {
                    throw new IllegalArgumentException("First Name and First Surname are mandatory for non-NIT documents");
                }
            }

            // 4. Date Logic: annotationDate >= deedDate
            if (dto.annotationDate() != null && dto.deedDate() != null) {
                if (dto.annotationDate().isBefore(dto.deedDate())) {
                    throw new IllegalArgumentException("Annotation date cannot be before deed date");
                }
            }

            // 5. Deed dependencies
            if (dto.deedDate() != null && (dto.deedNumber() == null || dto.deedNumber().isBlank())) {
                throw new IllegalArgumentException("Deed number is mandatory if deed date is provided");
            }
            if (dto.deedNumber() != null && !dto.deedNumber().isBlank() && (dto.notary() == null || dto.notary().isBlank())) {
                throw new IllegalArgumentException("Notary is mandatory if deed number is provided");
            }

            // 6. Informalidad Logic
            if ("INFORMALIDAD".equalsIgnoreCase(dto.titularityType())) {
                if (dto.observations() == null || dto.observations().isBlank()) {
                    throw new IllegalArgumentException("Observations are mandatory for Informalidad");
                }
            }
        }

        // RNG-Sum: Total must be exactly 100%
        if (Math.abs(totalPercent - 100.0) > 0.001) {
            throw new IllegalArgumentException("Total ownership percentage must sum exactly 100%. Current sum: " + totalPercent);
        }
    }

    private Titular mapDtoToEntity(TitularRequest dto, Predio predio) {
        Titular titular = new Titular();
        titular.setPredio(predio);
        titular.setTitularidadType(Titular.TitularityType.valueOf(dto.titularityType().toUpperCase()));
        titular.setDocumentType(Titular.DocumentType.valueOf(dto.documentType().toUpperCase()));
        titular.setDocumentNumber(dto.documentNumber());
        titular.setRightPercent(dto.rightPercent());
        
        titular.setFirstName(dto.firstName());
        titular.setMiddleName(dto.middleName());
        titular.setFirstSurname(dto.firstSurname());
        titular.setSecondSurname(dto.secondSurname());
        titular.setSocialReason(dto.socialReason());
        
        titular.setDeedNumber(dto.deedNumber());
        titular.setDeedDate(dto.deedDate());
        titular.setAnnotationDate(dto.annotationDate());
        titular.setPromiseDate(dto.promiseDate());
        titular.setTrustCode(dto.trustCode());
        titular.setNotary(dto.notary());
        titular.setObservations(dto.observations());
        
        return titular;
    }
}
