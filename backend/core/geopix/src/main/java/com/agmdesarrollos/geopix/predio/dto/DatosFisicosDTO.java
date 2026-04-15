package com.agmdesarrollos.geopix.predio.dto;

import java.math.BigDecimal;

public class DatosFisicosDTO {
    private BigDecimal areaVurHa;
    private BigDecimal areaVurM2;
    private BigDecimal areaCatastroHa;
    private BigDecimal areaCatastroM2;
    private BigDecimal areaEscriturasHa;
    private BigDecimal areaEscriturasM2;
    private BigDecimal areaMedicacionHa;
    private BigDecimal areaMedicacionM2;
    private String areaTotal;

    public BigDecimal getAreaVurHa() {
        return areaVurHa;
    }

    public void setAreaVurHa(BigDecimal areaVurHa) {
        this.areaVurHa = areaVurHa;
    }

    public BigDecimal getAreaVurM2() {
        return areaVurM2;
    }

    public void setAreaVurM2(BigDecimal areaVurM2) {
        this.areaVurM2 = areaVurM2;
    }

    public BigDecimal getAreaCatastroHa() {
        return areaCatastroHa;
    }

    public void setAreaCatastroHa(BigDecimal areaCatastroHa) {
        this.areaCatastroHa = areaCatastroHa;
    }

    public BigDecimal getAreaCatastroM2() {
        return areaCatastroM2;
    }

    public void setAreaCatastroM2(BigDecimal areaCatastroM2) {
        this.areaCatastroM2 = areaCatastroM2;
    }

    public BigDecimal getAreaEscriturasHa() {
        return areaEscriturasHa;
    }

    public void setAreaEscriturasHa(BigDecimal areaEscriturasHa) {
        this.areaEscriturasHa = areaEscriturasHa;
    }

    public BigDecimal getAreaEscriturasM2() {
        return areaEscriturasM2;
    }

    public void setAreaEscriturasM2(BigDecimal areaEscriturasM2) {
        this.areaEscriturasM2 = areaEscriturasM2;
    }

    public BigDecimal getAreaMedicacionHa() {
        return areaMedicacionHa;
    }

    public void setAreaMedicacionHa(BigDecimal areaMedicacionHa) {
        this.areaMedicacionHa = areaMedicacionHa;
    }

    public BigDecimal getAreaMedicacionM2() {
        return areaMedicacionM2;
    }

    public void setAreaMedicacionM2(BigDecimal areaMedicacionM2) {
        this.areaMedicacionM2 = areaMedicacionM2;
    }

    public String getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(String areaTotal) {
        this.areaTotal = areaTotal;
    }
}