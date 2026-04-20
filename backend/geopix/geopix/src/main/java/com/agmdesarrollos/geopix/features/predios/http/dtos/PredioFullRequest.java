package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.Valid;

/**
 * Unified request for creating or updating a Predio with all its sections.
 * All sections are optional at the root level, but internal validations apply if present.
 */
public record PredioFullRequest(
        @Valid
        DatosBasicosPredioRequest basicData,

        @Valid
        DatosFisicosPredioRequest physicalData,

        @Valid
        TitularesPredioRequest titularity
) {
}
