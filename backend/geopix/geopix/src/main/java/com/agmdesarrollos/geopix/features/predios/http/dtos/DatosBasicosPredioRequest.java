package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.constraints.*;
import java.util.List;

public record DatosBasicosPredioRequest(

        @NotBlank(message = "La matrícula inmobiliaria es obligatoria")
        @Size(max = 30, message = "La matrícula no debe exceder los 30 caracteres")
        @Pattern(regexp = "^\\d+$", message = "La matrícula debe ser numérica")
        String realEstateRegistration,

        @Size(max = 30, message = "El NPN no debe exceder los 30 caracteres")
        @Pattern(regexp = "^\\d*$", message = "El NPN debe ser numérico")
        String nationalPropertyNumber,

        @Size(max = 30, message = "La referencia catastral no debe exceder los 30 caracteres")
        String cadastralReference,

        @Size(max = 30, message = "La referencia NPN GEO no debe exceder los 30 caracteres")
        String nationalPropertyNumberGeo,

        @NotNull(message = "El departamento es obligatorio")
        Long departmentId,

        @NotNull(message = "El municipio es obligatorio")
        Long municipalityId,

        @NotEmpty(message = "Debe seleccionar al menos un tipo de predio")
        List<Long> propertyConditions,

        @NotBlank(message = "Debe especificar si tiene servidumbre")
        @Pattern(regexp = "^(SI|NO|SIN INFORMACION)$", message = "Valores permitidos: SI, NO, SIN INFORMACION")
        String hasEasement,

        // Validación condicional recomendada en Service: Obligatorio si hasEasement == "SI"
        Long easementTypeId,

        @NotNull(message = "El tipo de suelo es obligatorio")
        Long soilTypeId,

        @NotNull(message = "El destino es obligatorio")
        Long destinationTypeId,

        @NotNull(message = "El tipo de titularidad es obligatorio")
        Long ownershipTypeId,

        @NotNull(message = "El tipo de expediente es obligatorio")
        Long recordTypeId,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255, message = "La dirección no debe exceder los 255 caracteres")
        String address,

        @NotBlank(message = "El alias es obligatorio")
        @Size(max = 255, message = "El alias no debe exceder los 255 caracteres")
        String alias,

        @NotNull(message = "La afectación ambiental es obligatoria")
        Boolean environmentalImpact,

        @Size(max = 300, message = "Las observaciones no deben exceder los 300 caracteres")
        String observations
) {
    /**
     * Constructor compacto para aplicar la lógica de "SIN INFORMACIÓN"
     * en campos opcionales que lleguen nulos o vacíos.
     */
    public DatosBasicosPredioRequest {
        cadastralReference = (cadastralReference == null || cadastralReference.isBlank()) ? "SIN INFORMACION" : cadastralReference;
        nationalPropertyNumberGeo = (nationalPropertyNumberGeo == null || nationalPropertyNumberGeo.isBlank()) ? "SIN INFORMACION" : nationalPropertyNumberGeo;
        observations = (observations == null || observations.isBlank()) ? "SIN INFORMACION" : observations;
        // El NPN según tabla es NO obligatorio pero permite edición
        nationalPropertyNumber = (nationalPropertyNumber == null || nationalPropertyNumber.isBlank()) ? "SIN INFORMACION" : nationalPropertyNumber;
    }
}