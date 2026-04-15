package com.agmdesarrollos.geopix.predio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InfoEconomicaDTO {
    private Integer vigencia;
    private LocalDate fechaReferenciaPago;
    private BigDecimal avaluoCatastral;
    private BigDecimal impuestoPredial;
    private BigDecimal intereses;

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public LocalDate getFechaReferenciaPago() {
        return fechaReferenciaPago;
    }

    public void setFechaReferenciaPago(LocalDate fechaReferenciaPago) {
        this.fechaReferenciaPago = fechaReferenciaPago;
    }

    public BigDecimal getAvaluoCatastral() {
        return avaluoCatastral;
    }

    public void setAvaluoCatastral(BigDecimal avaluoCatastral) {
        this.avaluoCatastral = avaluoCatastral;
    }

    public BigDecimal getImpuestoPredial() {
        return impuestoPredial;
    }

    public void setImpuestoPredial(BigDecimal impuestoPredial) {
        this.impuestoPredial = impuestoPredial;
    }

    public BigDecimal getIntereses() {
        return intereses;
    }

    public void setIntereses(BigDecimal intereses) {
        this.intereses = intereses;
    }
}