package com.agmdesarrollos.geopix.predio.dto;

import java.math.BigDecimal;

public class GeoreferenciaDTO {
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String vertices;
    private String areaCalculada;
    private String perimetro;

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getVertices() {
        return vertices;
    }

    public void setVertices(String vertices) {
        this.vertices = vertices;
    }

    public String getAreaCalculada() {
        return areaCalculada;
    }

    public void setAreaCalculada(String areaCalculada) {
        this.areaCalculada = areaCalculada;
    }

    public String getPerimetro() {
        return perimetro;
    }

    public void setPerimetro(String perimetro) {
        this.perimetro = perimetro;
    }
}