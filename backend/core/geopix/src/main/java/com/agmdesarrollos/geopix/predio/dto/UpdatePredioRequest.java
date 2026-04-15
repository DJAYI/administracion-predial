package com.agmdesarrollos.geopix.predio.dto;

import java.util.List;

public class UpdatePredioRequest {
    private DatosBasicosDTO datosBasicos;
    private DatosFisicosDTO datosFisicos;
    private List<TitularDTO> titulares;
    private GeoreferenciaDTO georeferencia;
    private InfoEconomicaDTO infoEconomica;

    public DatosBasicosDTO getDatosBasicos() {
        return datosBasicos;
    }

    public void setDatosBasicos(DatosBasicosDTO datosBasicos) {
        this.datosBasicos = datosBasicos;
    }

    public DatosFisicosDTO getDatosFisicos() {
        return datosFisicos;
    }

    public void setDatosFisicos(DatosFisicosDTO datosFisicos) {
        this.datosFisicos = datosFisicos;
    }

    public List<TitularDTO> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<TitularDTO> titulares) {
        this.titulares = titulares;
    }

    public GeoreferenciaDTO getGeoreferencia() {
        return georeferencia;
    }

    public void setGeoreferencia(GeoreferenciaDTO georeferencia) {
        this.georeferencia = georeferencia;
    }

    public InfoEconomicaDTO getInfoEconomica() {
        return infoEconomica;
    }

    public void setInfoEconomica(InfoEconomicaDTO infoEconomica) {
        this.infoEconomica = infoEconomica;
    }
}