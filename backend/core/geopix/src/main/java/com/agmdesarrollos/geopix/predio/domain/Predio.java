package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "predios")
public class Predio {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "estado_registro", length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro = EstadoRegistro.BORRADOR;

    @Column(name = "paso_actual")
    private Integer pasoActual = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Embedded
    private DatosBasicos datosBasicos;

    @Embedded
    private DatosFisicos datosFisicos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "predio_id")
    private List<Titular> titulares = new ArrayList<>();

    @Embedded
    private Georeferencia georeferencia;

    @Embedded
    private InfoEconomica infoEconomica;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EstadoRegistro getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(EstadoRegistro estadoRegistro) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DatosBasicos getDatosBasicos() {
        return datosBasicos;
    }

    public void setDatosBasicos(DatosBasicos datosBasicos) {
        this.datosBasicos = datosBasicos;
    }

    public DatosFisicos getDatosFisicos() {
        return datosFisicos;
    }

    public void setDatosFisicos(DatosFisicos datosFisicos) {
        this.datosFisicos = datosFisicos;
    }

    public List<Titular> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<Titular> titulares) {
        this.titulares = titulares;
    }

    public Georeferencia getGeoreferencia() {
        return georeferencia;
    }

    public void setGeoreferencia(Georeferencia georeferencia) {
        this.georeferencia = georeferencia;
    }

    public InfoEconomica getInfoEconomica() {
        return infoEconomica;
    }

    public void setInfoEconomica(InfoEconomica infoEconomica) {
        this.infoEconomica = infoEconomica;
    }

    public void avanzarPaso(Integer siguientePaso) {
        if (siguientePaso != null && siguientePaso > this.pasoActual) {
            this.pasoActual = siguientePaso;
        }
    }

    public boolean puedeAvanzarA(Integer paso) {
        return paso != null && paso == this.pasoActual + 1;
    }

    public boolean estaCompleto() {
        return this.estadoRegistro == EstadoRegistro.COMPLETO 
            || this.estadoRegistro == EstadoRegistro.ACTIVO;
    }
}