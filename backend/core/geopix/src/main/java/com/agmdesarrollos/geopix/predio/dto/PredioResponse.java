package com.agmdesarrollos.geopix.predio.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PredioResponse {
    private String id;
    private String estadoRegistro;
    private Integer pasoActual;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DatosBasicosDTO datosBasicos;
    private DatosFisicosDTO datosFisicos;
    private List<TitularDTO> titulares;
    private GeoreferenciaDTO georeferencia;
    private InfoEconomicaDTO infoEconomica;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public Integer getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(Integer pasoActual) {
        this.pasoActual = pasoActual;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

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