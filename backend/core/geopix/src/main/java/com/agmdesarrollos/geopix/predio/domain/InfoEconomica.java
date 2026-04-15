package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
public class InfoEconomica {

    @Column(name = "vigencia")
    private Integer vigencia;

    @Column(name = "fecha_referencia_pago")
    private LocalDate fechaReferenciaPago;

    @Column(name = "avaluo_catastral")
    private BigDecimal avaluoCatastral;

    @Column(name = "impuesto_predial")
    private BigDecimal impuestoPredial;

    @Column(name = "intereses")
    private BigDecimal intereses;

    public InfoEconomica() {}

    public boolean tieneDatosCompletos() {
        return vigencia != null 
            && avaluoCatastral != null 
            && avaluoCatastral.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean tieneVigenciaValida() {
        if (vigencia == null) {
            return false;
        }
        int anioActual = LocalDate.now().getYear();
        return vigencia <= anioActual;
    }

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