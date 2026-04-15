package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "predio_vigencias")
public class PredioVigencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "predio_id", nullable = false)
    private String prediovId;

    @Column(name = "vigencia", nullable = false)
    private Integer vigencia;

    @Column(name = "numero_inmueble")
    private String numeroInmueble;

    @Column(name = "fecha_ref_pago")
    private LocalDate fechaRefPago;

    @Column(name = "referencia_pagos")
    private String referenciaPagos;

    @Column(name = "avaluo_catastral")
    private BigDecimal avaluoCatastral;

    @Column(name = "impuesto_predial")
    private BigDecimal impuestoPredial;

    @Column(name = "intereses_predial")
    private BigDecimal interesesPredial;

    @Column(name = "sac")
    private BigDecimal sac;

    @Column(name = "intereses_sac")
    private BigDecimal interesesSac;

    @Column(name = "descuentos")
    private BigDecimal descuentos;

    @Column(name = "sobretasa_ambiental")
    private BigDecimal sobretasaAmbiental;

    @Column(name = "tarifas")
    private BigDecimal tarifas;

    @Column(name = "_total", nullable = false)
    private BigDecimal total;

    @Column(name = "estado", length = 20)
    private String estado = "EN_DEUDA";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        calcularTotal();
    }

    public void calcularTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        
        if (impuestoPredial != null) sum = sum.add(impuestoPredial);
        if (interesesPredial != null) sum = sum.add(interesesPredial);
        if (sac != null) sum = sum.add(sac);
        if (interesesSac != null) sum = sum.add(interesesSac);
        if (sobretasaAmbiental != null) sum = sum.add(sobretasaAmbiental);
        if (tarifas != null) sum = sum.add(tarifas);
        if (descuentos != null) sum = sum.subtract(descuentos);
        
        this.total = sum;
        
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            this.estado = "PAGADO";
        } else {
            this.estado = "EN_DEUDA";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrediovId() {
        return prediovId;
    }

    public void setPrediovId(String prediovId) {
        this.prediovId = prediovId;
    }

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}