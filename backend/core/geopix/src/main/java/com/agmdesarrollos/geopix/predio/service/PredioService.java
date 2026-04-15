package com.agmdesarrollos.geopix.predio.service;

import com.agmdesarrollos.geopix.predio.domain.*;
import com.agmdesarrollos.geopix.predio.dto.*;
import com.agmdesarrollos.geopix.predio.jpa.JpaPredioRepository;
import com.agmdesarrollos.geopix.predio.jpa.JpaTitularRepository;
import com.agmdesarrollos.geopix.predio.exception.PredioNotFoundException;
import com.agmdesarrollos.geopix.predio.exception.InvalidTransitionException;
import com.agmdesarrollos.geopix.predio.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredioService {

    private final JpaPredioRepository PredioRepository;
    private final JpaTitularRepository TitularRepository;

    public PredioService(JpaPredioRepository PredioRepository, JpaTitularRepository titularRepository) {
        this.PredioRepository = PredioRepository;
        this.TitularRepository = titularRepository;
    }

    @Transactional
    public PredioResponse crearDraft(CreatePredioRequest request, String usuario) {
        Predio Predio = new Predio();
        Predio.setCreatedBy(usuario);
        Predio.setEstadoRegistro(EstadoRegistro.BORRADOR);
        Predio.setPasoActual(1);

        DatosBasicos datosBasicos = mapearDatosBasicos(request.getDatosBasicos());
        Predio.setDatosBasicos(datosBasicos);

        if (request.getDatosBasicos() != null && request.getDatosBasicos().getMatriculaInmobiliaria() != null) {
            String matricula = datosBasicos.getMatriculaInmobiliaria();
            if (PredioRepository.existsByMatriculaInmobiliaria(matricula)) {
                throw new ValidationException("La matrícula inmobiliaria ya existe en el sistema");
            }
        }

        Predio = PredioRepository.save(Predio);
        return toResponse(Predio);
    }

    @Transactional(readOnly = true)
    public PredioResponse obtenerDraft(String id) {
        Predio Predio = PredioRepository.findById(id)
            .orElseThrow(() -> new PredioNotFoundException("Borrador no encontrado: " + id));
        return toResponse(Predio);
    }

    @Transactional
    public PredioResponse actualizarPaso(String id, UpdatePredioRequest request, Integer paso, String usuario) {
        Predio Predio = PredioRepository.findById(id)
            .orElseThrow(() -> new PredioNotFoundException("Borrador no encontrado: " + id));

        if (!Predio.puedeAvanzarA(paso)) {
            throw new InvalidTransitionException("No puede editar el paso " + paso + " desde el paso actual " + Predio.getPasoActual());
        }

        switch ( paso) {
            case 1 -> actualizarDatosBasicos(Predio, request.getDatosBasicos());
            case 2 -> actualizarDatosFisicos(Predio, request.getDatosFisicos());
            case 3 -> actualizarTitulares(Predio, request.getTitulares());
            case 4 -> actualizarGeoreferencia(Predio, request.getGeoreferencia());
            case 5 -> actualizarInfoEconomica(Predio, request.getInfoEconomica());
            default -> throw new ValidationException("Paso inválido: " + paso);
        }

        Predio.avanzarPaso(paso);
        Predio = PredioRepository.save(Predio);
        return toResponse(Predio);
    }

    @Transactional
    public PredioResponse completarPredio(String id, String usuario) {
        Predio Predio = PredioRepository.findById(id)
            .orElseThrow(() -> new PredioNotFoundException("Predio no encontrado: " + id));

        if (Predio.getPasoActual() != 5) {
            throw new InvalidTransitionException("Debe completar el paso 5 antes de finalizar el registro");
        }

        validarCompletitud(Predio);

        Predio.setEstadoRegistro(EstadoRegistro.COMPLETO);
        Predio = PredioRepository.save(Predio);
        return toResponse(Predio);
    }

    @Transactional(readOnly = true)
    public boolean existeMatricula(String matricula) {
        return PredioRepository.existsByMatriculaInmobiliaria(matricula);
    }

    @Transactional(readOnly = true)
    public boolean verificarMatriculaDisponible(String matricula, String excludeId) {
        return PredioRepository.findByMatriculaExcludingId(matricula, excludeId).isEmpty();
    }

    private void actualizarDatosFisicos(Predio Predio, DatosFisicosDTO dto) {
        if (dto == null) return;
        
        DatosFisicos datos = new DatosFisicos();
        if (dto.getAreaVurHa() != null) datos.setAreaVurHa(dto.getAreaVurHa());
        if (dto.getAreaVurM2() != null) datos.setAreaVurM2(dto.getAreaVurM2());
        if (dto.getAreaCatastroHa() != null) datos.setAreaCatastroHa(dto.getAreaCatastroHa());
        if (dto.getAreaCatastroM2() != null) datos.setAreaCatastroM2(dto.getAreaCatastroM2());
        if (dto.getAreaEscriturasHa() != null) datos.setAreaEscriturasHa(dto.getAreaEscriturasHa());
        if (dto.getAreaEscriturasM2() != null) datos.setAreaEscriturasM2(dto.getAreaEscriturasM2());
        if (dto.getAreaMedicacionHa() != null) datos.setAreaMedicacionHa(dto.getAreaMedicacionHa());
        if (dto.getAreaMedicacionM2() != null) datos.setAreaMedicacionM2(dto.getAreaMedicacionM2());
        
        datos.setAreaTotal(datos.calcularAreaTotal());
        Predio.setDatosFisicos(datos);
    }

    private void actualizarTitulares(Predio Predio, List<TitularDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return;
        
        Predio.getTitulares().clear();
        
        for (TitularDTO dto : dtos) {
            Titular titular = new Titular();
            titular.setPrediovId(Predio.getId());
            titular.setTipoDocumento(dto.getTipoDocumento());
            titular.setDocumento(dto.getDocumento());
            titular.setRazonSocial(dto.getRazonSocial());
            titular.setNombres(dto.getNombres());
            titular.setApellidos(dto.getApellidos());
            titular.setDerechoPorcentaje(dto.getDerechoPorcentaje());
            titular.setEscritura(dto.getEscritura());
            titular.setFechaEscritura(dto.getFechaEscritura());
            titular.setTipoTitularidad(dto.getTipoTitularidad());
            
            Predio.getTitulares().add(titular);
        }
    }

    private void actualizarGeoreferencia(Predio Predio, GeoreferenciaDTO dto) {
        if (dto == null) return;
        
        Georeferencia geo = new Georeferencia();
        if (dto.getLatitud() != null) geo.setLatitud(dto.getLatitud());
        if (dto.getLongitud() != null) geo.setLongitud(dto.getLongitud());
        if (dto.getVertices() != null) geo.setVertices(dto.getVertices());
        if (dto.getAreaCalculada() != null) geo.setAreaCalculada(dto.getAreaCalculada());
        if (dto.getPerimetro() != null) geo.setPerimetro(dto.getPerimetro());
        
        Predio.setGeoreferencia(geo);
    }

    private void actualizarDatosBasicos(Predio Predio, DatosBasicosDTO dto) {
        if (dto == null) return;
        
        DatosBasicos datos = Predio.getDatosBasicos();
        if (datos == null) {
            datos = new DatosBasicos();
            Predio.setDatosBasicos(datos);
        }
        
        if (dto.getMatriculaInmobiliaria() != null) datos.setMatriculaInmobiliaria(sanitize(dto.getMatriculaInmobiliaria()));
        if (dto.getNpn() != null) datos.setNpn(dto.getNpn());
        if (dto.getReferenciaCatastral() != null) datos.setReferenciaCatastral(dto.getReferenciaCatastral());
        if (dto.getDepartamento() != null) datos.setDepartamento(dto.getDepartamento());
        if (dto.getMunicipio() != null) datos.setMunicipio(dto.getMunicipio());
        if (dto.getTipoPredio() != null) datos.setTipoPredio(dto.getTipoPredio());
        if (dto.getDestino() != null) datos.setDestino(dto.getDestino());
        if (dto.getDireccion() != null) datos.setDireccion(sanitize(dto.getDireccion()));
        if (dto.getTipoSuelo() != null) datos.setTipoSuelo(dto.getTipoSuelo());
        if (dto.getServidumbre() != null) datos.setServidumbre(dto.getServidumbre());
        if (dto.getTipoServidumbre() != null) datos.setTipoServidumbre(dto.getTipoServidumbre());
        if (dto.getAfectacion() != null) datos.setAfectacion(dto.getAfectacion());
    }

    private void actualizarInfoEconomica(Predio Predio, InfoEconomicaDTO dto) {
        if (dto == null) return;
        
        InfoEconomica info = new InfoEconomica();
        if (dto.getVigencia() != null) info.setVigencia(dto.getVigencia());
        if (dto.getFechaReferenciaPago() != null) info.setFechaReferenciaPago(dto.getFechaReferenciaPago());
        if (dto.getAvaluoCatastral() != null) info.setAvaluoCatastral(dto.getAvaluoCatastral());
        if (dto.getImpuestoPredial() != null) info.setImpuestoPredial(dto.getImpuestoPredial());
        if (dto.getIntereses() != null) info.setIntereses(dto.getIntereses());
        
        Predio.setInfoEconomica(info);
    }

    private void validarCompletitud(Predio Predio) {
        if (Predio.getDatosBasicos() == null 
            || Predio.getDatosBasicos().getMatriculaInmobiliaria() == null) {
            throw new ValidationException("Datos básicos incompletos");
        }

        BigDecimal sumaPorcentajes = BigDecimal.ZERO;
        for (Titular t : Predio.getTitulares()) {
            if (t.getDerechoPorcentaje() != null) {
                sumaPorcentajes = sumaPorcentajes.add(t.getDerechoPorcentaje());
            }
        }
        
        if (!Predio.getTitulares().isEmpty() 
            && sumaPorcentajes.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ValidationException("La suma de porcentajes de derecho no puede superar el 100%");
        }
    }

    private DatosBasicos mapearDatosBasicos(DatosBasicosDTO dto) {
        DatosBasicos datos = new DatosBasicos();
        if (dto == null) return datos;
        
        datos.setMatriculaInmobiliaria(sanitize(dto.getMatriculaInmobiliaria()));
        datos.setNpn(dto.getNpn());
        datos.setReferenciaCatastral(dto.getReferenciaCatastral());
        datos.setDepartamento(dto.getDepartamento());
        datos.setMunicipio(dto.getMunicipio());
        datos.setTipoPredio(dto.getTipoPredio());
        datos.setDestino(dto.getDestino());
        datos.setDireccion(sanitize(dto.getDireccion()));
        datos.setTipoSuelo(dto.getTipoSuelo());
        datos.setServidumbre(dto.getServidumbre());
        datos.setTipoServidumbre(dto.getTipoServidumbre());
        datos.setAfectacion(dto.getAfectacion());
        
        return datos;
    }

    private PredioResponse toResponse(Predio Predio) {
        PredioResponse response = new PredioResponse();
        response.setId(Predio.getId());
        response.setEstadoRegistro(Predio.getEstadoRegistro().name());
        response.setPasoActual(Predio.getPasoActual());
        response.setCreatedAt(Predio.getCreatedAt());
        response.setUpdatedAt(Predio.getUpdatedAt());
        
        if (Predio.getDatosBasicos() != null) {
            response.setDatosBasicos(toDatosBasicosDTO(Predio.getDatosBasicos()));
        }
        
        if (Predio.getDatosFisicos() != null) {
            response.setDatosFisicos(toDatosFisicosDTO(Predio.getDatosFisicos()));
        }
        
        if (!Predio.getTitulares().isEmpty()) {
            response.setTitulares(Predio.getTitulares().stream()
                .map(this::toTitularDTO)
                .collect(Collectors.toList()));
        }
        
        if (Predio.getGeoreferencia() != null) {
            response.setGeoreferencia(toGeoreferenciaDTO(Predio.getGeoreferencia()));
        }
        
        if (Predio.getInfoEconomica() != null) {
            response.setInfoEconomica(toInfoEconomicaDTO(Predio.getInfoEconomica()));
        }
        
        return response;
    }

    private DatosBasicosDTO toDatosBasicosDTO(DatosBasicos datos) {
        DatosBasicosDTO dto = new DatosBasicosDTO();
        dto.setMatriculaInmobiliaria(datos.getMatriculaInmobiliaria());
        dto.setNpn(datos.getNpn());
        dto.setReferenciaCatastral(datos.getReferenciaCatastral());
        dto.setDepartamento(datos.getDepartamento());
        dto.setMunicipio(datos.getMunicipio());
        dto.setTipoPredio(datos.getTipoPredio());
        dto.setDestino(datos.getDestino());
        dto.setDireccion(datos.getDireccion());
        dto.setTipoSuelo(datos.getTipoSuelo());
        dto.setServidumbre(datos.getServidumbre());
        dto.setTipoServidumbre(datos.getTipoServidumbre());
        dto.setAfectacion(datos.getAfectacion());
        return dto;
    }

    private DatosFisicosDTO toDatosFisicosDTO(DatosFisicos datos) {
        DatosFisicosDTO dto = new DatosFisicosDTO();
        dto.setAreaVurHa(datos.getAreaVurHa());
        dto.setAreaVurM2(datos.getAreaVurM2());
        dto.setAreaCatastroHa(datos.getAreaCatastroHa());
        dto.setAreaCatastroM2(datos.getAreaCatastroM2());
        dto.setAreaEscriturasHa(datos.getAreaEscriturasHa());
        dto.setAreaEscriturasM2(datos.getAreaEscriturasM2());
        dto.setAreaMedicacionHa(datos.getAreaMedicacionHa());
        dto.setAreaMedicacionM2(datos.getAreaMedicacionM2());
        dto.setAreaTotal(datos.getAreaTotal());
        return dto;
    }

    private TitularDTO toTitularDTO(Titular titular) {
        TitularDTO dto = new TitularDTO();
        dto.setId(titular.getId());
        dto.setTipoDocumento(titular.getTipoDocumento());
        dto.setDocumento(titular.getDocumento());
        dto.setRazonSocial(titular.getRazonSocial());
        dto.setNombres(titular.getNombres());
        dto.setApellidos(titular.getApellidos());
        dto.setDerechoPorcentaje(titular.getDerechoPorcentaje());
        dto.setEscritura(titular.getEscritura());
        dto.setFechaEscritura(titular.getFechaEscritura());
        dto.setTipoTitularidad(titular.getTipoTitularidad());
        return dto;
    }

    private GeoreferenciaDTO toGeoreferenciaDTO(Georeferencia geo) {
        GeoreferenciaDTO dto = new GeoreferenciaDTO();
        dto.setLatitud(geo.getLatitud());
        dto.setLongitud(geo.getLongitud());
        dto.setVertices(geo.getVertices());
        dto.setAreaCalculada(geo.getAreaCalculada());
        dto.setPerimetro(geo.getPerimetro());
        return dto;
    }

    private InfoEconomicaDTO toInfoEconomicaDTO(InfoEconomica info) {
        InfoEconomicaDTO dto = new InfoEconomicaDTO();
        dto.setVigencia(info.getVigencia());
        dto.setFechaReferenciaPago(info.getFechaReferenciaPago());
        dto.setAvaluoCatastral(info.getAvaluoCatastral());
        dto.setImpuestoPredial(info.getImpuestoPredial());
        dto.setIntereses(info.getIntereses());
        return dto;
    }

    private String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"'&]", "");
    }
}