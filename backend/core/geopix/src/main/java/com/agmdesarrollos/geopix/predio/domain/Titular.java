package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "predio_titulares")
public class Titular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "predio_id", nullable = false)
    private String prediovId;

    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento;

    @Column(name = "documento", length = 50)
    private String documento;

    @Column(name = "razon_social", length = 255)
    private String razonSocial;

    @Column(name = "nombres", length = 100)
    private String nombres;

    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @Column(name = "derecho_porcentaje")
    private BigDecimal derechoPorcentaje;

    @Column(name = "escritura", length = 50)
    private String escritura;

    @Column(name = "fecha_escritura")
    private LocalDate fechaEscritura;

    @Column(name = "tipo_titularidad", length = 30)
    private String tipoTitularidad;

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