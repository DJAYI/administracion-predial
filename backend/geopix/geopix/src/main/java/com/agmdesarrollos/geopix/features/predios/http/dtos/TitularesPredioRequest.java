package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for bulk management of property owners.
 * Total 'rightPercent' sum must be exactly 100%.
 */
public record TitularesPredioRequest(
        @NotEmpty(message = "At least one owner is required if this section is sent")
        List<@Valid TitularRequest> owners
) {
}
