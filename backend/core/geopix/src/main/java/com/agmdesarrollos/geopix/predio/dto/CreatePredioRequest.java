package com.agmdesarrollos.geopix.predio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreatePredioRequest {

    @Valid
    @NotNull(message = "Datos básicos requeridos")
    private DatosBasicosDTO datosBasicos;

    public DatosBasicosDTO getDatosBasicos() {
        return datosBasicos;
    }

    public void setDatosBasicos(DatosBasicosDTO datosBasicos) {
        this.datosBasicos = datosBasicos;
    }
}