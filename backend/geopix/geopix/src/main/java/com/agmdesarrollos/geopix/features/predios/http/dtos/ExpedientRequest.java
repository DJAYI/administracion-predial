package com.agmdesarrollos.geopix.features.predios.http.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpedientRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
