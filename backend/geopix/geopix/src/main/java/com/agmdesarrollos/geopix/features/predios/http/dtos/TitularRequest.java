package com.agmdesarrollos.geopix.features.predios.http.dtos;


import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Individual owner DTO.
 * Includes basic validation and marks for conditional logic.
 */
public record TitularRequest(
        @NotBlank(message = "Titularity type is mandatory")
        String titularityType,

        @NotBlank(message = "Document type is mandatory")
        String documentType,

        @NotBlank(message = "Document number is mandatory")
        @Size(max = 15, message = "Document number must not exceed 15 characters")
        String documentNumber,

        @NotNull(message = "Right percentage is mandatory")
        @DecimalMin(value = "0.01", message = "Right percentage must be greater than 0")
        @DecimalMax(value = "100.00", message = "Right percentage cannot exceed 100")
        Double rightPercent,

        // COND: Mandatory if documentType is NIT
        @Size(max = 150)
        String socialReason,

        // COND: Mandatory if documentType is NOT NIT
        @Size(max = 50)
        String firstName,

        @Size(max = 50)
        String middleName,

        @Size(max = 50)
        String firstSurname,

        @Size(max = 50)
        String secondSurname,

        // COND: Required if deedDate exists
        @Size(max = 50)
        String deedNumber,

        LocalDate deedDate,

        // COND: Must be >= deedDate
        LocalDate annotationDate,

        // COND: Required if deedNumber exists
        @Size(max = 150)
        String notary,

        // COND: Only for "PROMESA" titularity
        LocalDate promiseDate,

        // COND: Only for fiduciary figures
        @Size(max = 50)
        String trustCode,

        // COND: Mandatory only for "INFORMALIDAD"
        @Size(max = 500)
        String observations
) {
}
