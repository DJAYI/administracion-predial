package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Georeferencia {

    @Column(name = "latitud")
    private BigDecimal latitud;

    @Column(name = "longitud")
    private BigDecimal longitud;

    @Column(name = "vertices", length = 2000)
    private String vertices;

    @Column(name = "area_calculada")
    private String areaCalculada;

    @Column(name = "perimetro")
    private String perimetro;

    public Georeferencia() {}

    public boolean tieneCoordenadas() {
        return latitud != null && longitud != null;
    }

    public boolean tienePoligono() {
        return vertices != null && !vertices.isBlank();
    }

    public boolean esPoligonoValido() {
        if (!tienePoligono()) {
            return false;
        }
        try {
            String[] verts = vertices.split(";");
            return verts.length >= 3;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean tieneCoordenadasValidasColombia() {
        if (!tieneCoordenadas()) {
            return false;
        }
        BigDecimal lat = latitud;
        BigDecimal lon = longitud;
        return lat.compareTo(BigDecimal.valueOf(-4)) >= 0 
            && lat.compareTo(BigDecimal.valueOf(12)) <= 0
            && lon.compareTo(BigDecimal.valueOf(-79)) >= 0
            && lon.compareTo(BigDecimal.valueOf(-66)) <= 0;
    }

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