package com.agmdesarrollos.geopix.predio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TitularDTO {
    private Long id;
    private String tipoDocumento;
    private String documento;
    private String razonSocial;
    private String nombres;
    private String apellidos;
    private BigDecimal derechoPorcentaje;
    private String escritura;
    private LocalDate fechaEscritura;
    private String tipoTitularidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public BigDecimal getDerechoPorcentaje() {
        return derechoPorcentaje;
    }

    public void setDerechoPorcentaje(BigDecimal derechoPorcentaje) {
        this.derechoPorcentaje = derechoPorcentaje;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }

    public LocalDate getFechaEscritura() {
        return fechaEscritura;
    }

    public void setFechaEscritura(LocalDate fechaEscritura) {
        this.fechaEscritura = fechaEscritura;
    }

    public String getTipoTitularidad() {
        return tipoTitularidad;
    }

    public void setTipoTitularidad(String tipoTitularidad) {
        this.tipoTitularidad = tipoTitularidad;
    }
}