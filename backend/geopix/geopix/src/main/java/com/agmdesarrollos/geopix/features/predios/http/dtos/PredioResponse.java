package com.agmdesarrollos.geopix.features.predios.http.dtos;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Titular;
import java.util.List;

/**
 * Unified response for Predio details.
 */
public record PredioResponse(
        Long id,
        String realEstateRegistration,
        String nationalPropertyNumber,
        String cadastralReference,
        String nationalPropertyNumberGeo,
        String departmentName,
        String municipalityName,
        List<String> propertyConditions,
        String hasEasement,
        String easementTypeName,
        String soilTypeName,
        String destinationTypeName,
        String ownershipTypeName,
        String recordTypeName,
        String address,
        String alias,
        Boolean environmentalImpact,
        String observations,

        // Physical Data
        Integer vurAreaHa, Integer vurAreaM2, String vurAreaTotal,
        Integer cadastralAreaHa, Integer cadastralAreaM2, String cadastralAreaTotal,
        Integer deedAreaHa, Integer deedAreaM2, String deedAreaTotal,
        Integer measuredAreaHa, Integer measuredAreaM2, String measuredAreaTotal,
        String boundariesAndDimensions,

        // Titularity
        List<TitularResponse> owners
) {
    public static PredioResponse fromEntity(Predio entity) {
        return new PredioResponse(
                entity.getId(),
                entity.getRealEstateRegistration(),
                entity.getNationalPropertyNumber(),
                entity.getCadastralReference(),
                entity.getNationalPropertyNumberGeo(),
                entity.getDepartment() != null ? entity.getDepartment().getName() : null,
                entity.getMunicipality() != null ? entity.getMunicipality().getName() : null,
                entity.getPropertyConditions() != null ? entity.getPropertyConditions().stream().map(c -> c.getName()).toList() : List.of(),
                entity.getHasEasement(),
                entity.getEasementType() != null ? entity.getEasementType().getName() : null,
                entity.getSoilType() != null ? entity.getSoilType().getName() : null,
                entity.getDestinationType() != null ? entity.getDestinationType().getName() : null,
                entity.getOwnershipType() != null ? entity.getOwnershipType().getName() : null,
                entity.getRecordType() != null ? entity.getRecordType().getName() : null,
                entity.getAddress(),
                entity.getAlias(),
                entity.getEnvironmentalImpact(),
                entity.getObservations(),
                entity.getVurAreaHa(), entity.getVurAreaM2(), entity.getVurAreaTotal(),
                entity.getCadastralAreaHa(), entity.getCadastralAreaM2(), entity.getCadastralAreaTotal(),
                entity.getDeedAreaHa(), entity.getDeedAreaM2(), entity.getDeedAreaTotal(),
                entity.getMeasuredAreaHa(), entity.getMeasuredAreaM2(), entity.getMeasuredAreaTotal(),
                entity.getBoundariesAndDimensions(),
                entity.getOwners() != null ? entity.getOwners().stream().map(TitularResponse::fromEntity).toList() : List.of()
        );
    }
}

record TitularResponse(
    Long id,
    String titularityType,
    String documentType,
    String documentNumber,
    Double rightPercent,
    String firstName,
    String middleName,
    String firstSurname,
    String secondSurname,
    String socialReason,
    String deedNumber,
    String deedDate,
    String annotationDate,
    String promiseDate,
    String trustCode,
    String notary,
    String observations
) {
    public static TitularResponse fromEntity(Titular entity) {
        return new TitularResponse(
            entity.getId(),
            entity.getTitularidadType().name(),
            entity.getDocumentType().name(),
            entity.getDocumentNumber(),
            entity.getRightPercent(),
            entity.getFirstName(),
            entity.getMiddleName(),
            entity.getFirstSurname(),
            entity.getSecondSurname(),
            entity.getSocialReason(),
            entity.getDeedNumber(),
            entity.getDeedDate() != null ? entity.getDeedDate().toString() : null,
            entity.getAnnotationDate() != null ? entity.getAnnotationDate().toString() : null,
            entity.getPromiseDate() != null ? entity.getPromiseDate().toString() : null,
            entity.getTrustCode(),
            entity.getNotary(),
            entity.getObservations()
        );
    }
}
