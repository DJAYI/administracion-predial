package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.constraints.*;
import java.util.List;

public record DatosBasicosPredioRequest(

        @NotBlank(message = "Real estate registration is mandatory")
        @Size(max = 30, message = "Registration must not exceed 30 characters")
        @Pattern(regexp = "^\\d+$", message = "Registration must be numeric")
        String realEstateRegistration,

        @Size(max = 30, message = "NPN must not exceed 30 characters")
        @Pattern(regexp = "^\\d*$", message = "NPN must be numeric")
        String nationalPropertyNumber,

        @Size(max = 30, message = "Cadastral reference must not exceed 30 characters")
        String cadastralReference,

        @Size(max = 30, message = "NPN GEO reference must not exceed 30 characters")
        String nationalPropertyNumberGeo,

        @NotNull(message = "Department is mandatory")
        Long departmentId,

        @NotNull(message = "Municipality is mandatory")
        Long municipalityId,

        @NotEmpty(message = "At least one property condition must be selected")
        List<Long> propertyConditions,

        @NotBlank(message = "Must specify if it has easement")
        @Pattern(regexp = "^(YES|NO|NO INFORMATION)$", message = "Allowed values: YES, NO, NO INFORMATION")
        String hasEasement,

        Long easementTypeId,

        @NotNull(message = "Soil type is mandatory")
        Long soilTypeId,

        @NotNull(message = "Destination is mandatory")
        Long destinationTypeId,

        @NotNull(message = "Ownership type is mandatory")
        Long ownershipTypeId,

        @NotNull(message = "Record type is mandatory")
        Long recordTypeId,

        @NotBlank(message = "Address is mandatory")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @NotBlank(message = "Alias is mandatory")
        @Size(max = 255, message = "Alias must not exceed 255 characters")
        String alias,

        @NotNull(message = "Environmental impact is mandatory")
        Boolean environmentalImpact,

        @Size(max = 300, message = "Observations must not exceed 300 characters")
        String observations
) {
    /**
     * Compact constructor to apply "NO INFORMATION" logic
     * for optional fields that arrive null or blank.
     */
    public DatosBasicosPredioRequest {
        cadastralReference = (cadastralReference == null || cadastralReference.isBlank()) ? "NO INFORMATION" : cadastralReference;
        nationalPropertyNumberGeo = (nationalPropertyNumberGeo == null || nationalPropertyNumberGeo.isBlank()) ? "NO INFORMATION" : nationalPropertyNumberGeo;
        observations = (observations == null || observations.isBlank()) ? "NO INFORMATION" : observations;
        nationalPropertyNumber = (nationalPropertyNumber == null || nationalPropertyNumber.isBlank()) ? "NO INFORMATION" : nationalPropertyNumber;
    }
}
