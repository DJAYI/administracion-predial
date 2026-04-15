package com.agmdesarrollos.geopix.predio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DatosBasicos {

    @Column(name = "matricula_inmobiliaria", length = 50)
    private String matriculaInmobiliaria;

    @Column(name = "npn", length = 50)
    private String npn;

    @Column(name = "referencia_catastral", length = 50)
    private String referenciaCatastral;

    @Column(name = "departamento", length = 10)
    private String departamento;

    @Column(name = "municipio", length = 10)
    private String municipio;

    @Column(name = "tipo_predio", length = 50)
    private String tipoPredio;

    @Column(name = "destino", length = 50)
    private String destino;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "tipo_suelo", length = 20)
    private String tipoSuelo;

    @Column(name = "servidumbre", length = 20)
    private String servidumbre;

    @Column(name = "tipo_servidumbre", length = 30)
    private String tipoServidumbre;

    @Column(name = "afectacion", length = 20)
    private String afectacion;

    public DatosBasicos() {}

    public String getMatriculaInmobiliaria() {
        return matriculaInmobiliaria;
    }

    public void setMatriculaInmobiliaria(String matriculaInmobiliaria) {
        this.matriculaInmobiliaria = matriculaInmobiliaria;
    }

    public String getNpn() {
        return npn;
    }

    public void setNpn(String npn) {
        this.npn = npn;
    }

    public String getReferenciaCatastral() {
        return referenciaCatastral;
    }

    public void setReferenciaCatastral(String referenciaCatastral) {
        this.referenciaCatastral = referenciaCatastral;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(String tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public String getServidumbre() {
        return servidumbre;
    }

    public void setServidumbre(String servidumbre) {
        this.servidumbre = servidumbre;
    }

    public String getTipoServidumbre() {
        return tipoServidumbre;
    }

    public void setTipoServidumbre(String tipoServidumbre) {
        this.tipoServidumbre = tipoServidumbre;
    }

    public String getAfectacion() {
        return afectacion;
    }

    public void setAfectacion(String afectacion) {
        this.afectacion = afectacion;
    }
}