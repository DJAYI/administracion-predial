package com.agmdesarrollos.geopix.predio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VigenciaRequestDTO {
    private Integer vigencia;
    private String numeroInmueble;
    private LocalDate fechaRefPago;
    private String referenciaPagos;
    private BigDecimal avaluoCatastral;
    private BigDecimal impuestoPredial;
    private BigDecimal interesesPredial;
    private BigDecimal sac;
    private BigDecimal interesesSac;
    private BigDecimal descuentos;
    private BigDecimal sobretasaAmbiental;
    private BigDecimal tarifas;

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public String getNumeroInmueble() {
        return numeroInmueble;
    }

    public void setNumeroInmueble(String numeroInmueble) {
        this.numeroInmueble = numeroInmueble;
    }

    public LocalDate getFechaRefPago() {
        return fechaRefPago;
    }

    public void setFechaRefPago(LocalDate fechaRefPago) {
        this.fechaRefPago = fechaRefPago;
    }

    public String getReferenciaPagos() {
        return referenciaPagos;
    }

    public void setReferenciaPagos(String referenciaPagos) {
        this.referenciaPagos = referenciaPagos;
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

    public BigDecimal getInteresesPredial() {
        return interesesPredial;
    }

    public void setInteresesPredial(BigDecimal interesesPredial) {
        this.interesesPredial = interesesPredial;
    }

    public BigDecimal getSac() {
        return sac;
    }

    public void setSac(BigDecimal sac) {
        this.sac = sac;
    }

    public BigDecimal getInteresesSac() {
        return interesesSac;
    }

    public void setInteresesSac(BigDecimal interesesSac) {
        this.interesesSac = interesesSac;
    }

    public BigDecimal getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(BigDecimal descuentos) {
        this.descuentos = descuentos;
    }

    public BigDecimal getSobretasaAmbiental() {
        return sobretasaAmbiental;
    }

    public void setSobretasaAmbiental(BigDecimal sobretasaAmbiental) {
        this.sobretasaAmbiental = sobretasaAmbiental;
    }

    public BigDecimal getTarifas() {
        return tarifas;
    }

    public void setTarifas(BigDecimal tarifas) {
        this.tarifas = tarifas;
    }
}