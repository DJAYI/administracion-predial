package com.agmdesarrollos.geopix.features.utils.logic.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MunicipioRawDTO(
        @JsonProperty("cod_mpio") String codMpio,
        @JsonProperty("dpto") String dpto,
        @JsonProperty("nom_mpio") String nomMpio
) {}