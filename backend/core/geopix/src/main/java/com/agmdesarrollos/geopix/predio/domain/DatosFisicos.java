package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class DatosFisicos {

    @Column(name = "area_vur_ha")
    private BigDecimal areaVurHa;

    @Column(name = "area_vur_m2")
    private BigDecimal areaVurM2;

    @Column(name = "area_catastro_ha")
    private BigDecimal areaCatastroHa;

    @Column(name = "area_catastro_m2")
    private BigDecimal areaCatastroM2;

    @Column(name = "area_escrituras_ha")
    private BigDecimal areaEscriturasHa;

    @Column(name = "area_escrituras_m2")
    private BigDecimal areaEscriturasM2;

    @Column(name = "area_medicion_ha")
    private BigDecimal areaMedicacionHa;

    @Column(name = "area_medicion_m2")
    private BigDecimal areaMedicacionM2;

    @Column(name = "area_total")
    private String areaTotal;

    public DatosFisicos() {}

    public boolean tieneDatosVur() {
        return areaVurHa != null || areaVurM2 != null;
    }

    public boolean tieneDatosCatastro() {
        return areaCatastroHa != null || areaCatastroM2 != null;
    }

    public boolean tieneDatosEscrituras() {
        return areaEscriturasHa != null || areaEscriturasM2 != null;
    }

    public boolean tieneDatosMedicacion() {
        return areaMedicacionHa != null || areaMedicacionM2 != null;
    }

    public boolean tieneAlgunaMedicion() {
        return tieneDatosVur() || tieneDatosCatastro() 
            || tieneDatosEscrituras() || tieneDatosMedicacion();
    }

    public String calcularAreaTotal() {
        if (!tieneAlgunaMedicion()) {
            return "SIN INFORMACION";
        }

        BigDecimal totalM2 = BigDecimal.ZERO;

        if (areaVurHa != null) {
            totalM2 = totalM2.add(areaVurHa.multiply(BigDecimal.valueOf(10000)));
        }
        if (areaVurM2 != null) {
            totalM2 = totalM2.add(areaVurM2);
        }

        if (areaCatastroHa != null) {
            totalM2 = totalM2.add(areaCatastroHa.multiply(BigDecimal.valueOf(10000)));
        }
        if (areaCatastroM2 != null) {
            totalM2 = totalM2.add(areaCatastroM2);
        }

        if (areaEscriturasHa != null) {
            totalM2 = totalM2.add(areaEscriturasHa.multiply(BigDecimal.valueOf(10000)));
        }
        if (areaEscriturasM2 != null) {
            totalM2 = totalM2.add(areaEscriturasM2);
        }

        if (areaMedicacionHa != null) {
            totalM2 = totalM2.add(areaMedicacionHa.multiply(BigDecimal.valueOf(10000)));
        }
        if (areaMedicacionM2 != null) {
            totalM2 = totalM2.add(areaMedicacionM2);
        }

        return totalM2.stripTrailingZeros().toPlainString();
    }

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