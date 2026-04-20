package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.constraints.*;

/**
 * DTO para la captura de Datos Físicos del Predio.
 * Sigue las reglas de negocio para áreas VUR, Catastro, Escrituras y Medición.
 */
public record DatosFisicosPredioRequest(

        // --- ÁREA VUR ---
        @Min(value = 0, message = "Hectáreas VUR no pueden ser negativas")
        @Digits(integer = 10, fraction = 0, message = "Máximo 10 dígitos para HA VUR")
        Integer vurAreaHa,

        @Min(value = 0, message = "Metros² VUR no pueden ser negativos")
        @Max(value = 9999, message = "Metros² VUR no pueden exceder 9.999 (RNG-01)")
        Integer vurAreaM2,

        // --- ÁREA CATASTRO ---
        @Min(value = 0, message = "Hectáreas Catastro no pueden ser negativas")
        @Digits(integer = 10, fraction = 0, message = "Máximo 10 dígitos para HA Catastro")
        Integer cadastralAreaHa,

        @Min(value = 0, message = "Metros² Catastro no pueden ser negativos")
        @Max(value = 9999, message = "Metros² Catastro no pueden exceder 9.999 (RNG-01)")
        Integer cadastralAreaM2,

        // --- ÁREA ESCRITURAS ---
        @Min(value = 0, message = "Hectáreas Escrituras no pueden ser negativas")
        @Digits(integer = 10, fraction = 0, message = "Máximo 10 dígitos para HA Escrituras")
        Integer deedAreaHa,

        @Min(value = 0, message = "Metros² Escrituras no pueden ser negativos")
        @Max(value = 9999, message = "Metros² Escrituras no pueden exceder 9.999 (RNG-01)")
        Integer deedAreaM2,

        // --- ÁREA MEDICIÓN ---
        @Min(value = 0, message = "Hectáreas Medición no pueden ser negativas")
        @Digits(integer = 10, fraction = 0, message = "Máximo 10 dígitos para HA Medición")
        Integer measuredAreaHa,

        @Min(value = 0, message = "Metros² Medición no pueden ser negativos")
        @Max(value = 9999, message = "Metros² Medición no pueden exceder 9.999 (RNG-01)")
        Integer measuredAreaM2,

        // --- LINDEROS ---
        @Size(max = 500, message = "Cabida y linderos no pueden exceder los 500 caracteres")
        String boundariesAndDimensions
) {
    /**
     * El cálculo del Área Total se realiza en la entidad Predio (@PrePersist/@PreUpdate)
     * basándose en la fórmula: (HA * 10.000) + M2.
     * Si HA o M2 son nulos, la entidad guardará "SIN INFORMACION".
     */
}
