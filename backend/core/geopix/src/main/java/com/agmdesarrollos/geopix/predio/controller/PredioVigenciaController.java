package com.agmdesarrollos.geopix.predio.controller;

import com.agmdesarrollos.geopix.predio.domain.PredioVigencia;
import com.agmdesarrollos.geopix.predio.dto.VigenciaRequestDTO;
import com.agmdesarrollos.geopix.predio.dto.VigenciaResponseDTO;
import com.agmdesarrollos.geopix.predio.exception.PredioNotFoundException;
import com.agmdesarrollos.geopix.predio.jpa.JpaPredioRepository;
import com.agmdesarrollos.geopix.predio.jpa.JpaPredioVigenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/predios/{predioId}/vigencias")
public class PredioVigenciaController {

    private final JpaPredioRepository PredioRepository;
    private final JpaPredioVigenciaRepository vigenciaRepository;

    public PredioVigenciaController(JpaPredioRepository PredioRepository,
                                   JpaPredioVigenciaRepository vigenciaRepository) {
        this.PredioRepository = PredioRepository;
        this.vigenciaRepository = vigenciaRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN', 'LECTOR')")
    public ResponseEntity<List<VigenciaResponseDTO>> listarVigencias(
            @PathVariable String Predioid) {
        
        if (!PredioRepository.existsById(Predioid)) {
            throw new PredioNotFoundException("Predio no encontrado: " + Predioid);
        }
        
        List<VigenciaResponseDTO> vigencias = vigenciaRepository
            .findByPrediovIdOrderByVigenciaDesc(Predioid)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(vigencias);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    @Transactional
    public ResponseEntity<VigenciaResponseDTO> crearVigencia(
            @PathVariable String Predioid,
            @RequestBody VigenciaRequestDTO request) {
        
        if (!PredioRepository.existsById(Predioid)) {
            throw new PredioNotFoundException("Predio no encontrado: " + Predioid);
        }
        
        PredioVigencia vigencia = new PredioVigencia();
        vigencia.setPrediovId(Predioid);
        mapearRequest(vigencia, request);
        
        vigencia = vigenciaRepository.save(vigencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(vigencia));
    }

    @PutMapping("/{vigenciaId}")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    @Transactional
    public ResponseEntity<VigenciaResponseDTO> actualizarVigencia(
            @PathVariable String Predioid,
            @PathVariable Long vigenciaId,
            @RequestBody VigenciaRequestDTO request) {
        
        PredioVigencia vigencia = vigenciaRepository.findById(vigenciaId)
            .orElseThrow(() -> new PredioNotFoundException("Vigencia no encontrada: " + vigenciaId));
        
        if (!vigencia.getPrediovId().equals(Predioid)) {
            throw new PredioNotFoundException("Vigencia no belongs a este predio");
        }
        
        mapearRequest(vigencia, request);
        vigencia = vigenciaRepository.save(vigencia);
        
        return ResponseEntity.ok(toResponseDTO(vigencia));
    }

    @DeleteMapping("/{vigenciaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> eliminarVigencia(
            @PathVariable String Predioid,
            @PathVariable Long vigenciaId) {
        
        PredioVigencia vigencia = vigenciaRepository.findById(vigenciaId)
            .orElseThrow(() -> new PredioNotFoundException("Vigencia no encontrada"));
        
        if (!vigencia.getPrediovId().equals(Predioid)) {
            throw new PredioNotFoundException("Vigencia no pertenece a este predio");
        }
        
        vigenciaRepository.delete(vigencia);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('EJECUTOR_INTEGRAL', 'ADMIN')")
    @Transactional
    public ResponseEntity<List<VigenciaResponseDTO>> crearVigenciasBatch(
            @PathVariable String predioid,
            @RequestBody List<VigenciaRequestDTO> requests) {
        
        if (!PredioRepository.existsById(predioid)) {
            throw new PredioNotFoundException("Predio no encontrado: " + predioid);
        }
        
        List<VigenciaResponseDTO> respuestas = new ArrayList<>();
        
        for (VigenciaRequestDTO request : requests) {
            PredioVigencia vigencia = new PredioVigencia();
            vigencia.setPrediovId(predioid);
            mapearRequest(vigencia, request);
            vigencia = vigenciaRepository.save(vigencia);
            respuestas.add(toResponseDTO(vigencia));
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(respuestas);
    }

    private void mapearRequest(PredioVigencia vigencia, VigenciaRequestDTO dto) {
        if (dto.getVigencia() != null) {
            vigencia.setVigencia(dto.getVigencia());
        }
        if (dto.getNumeroInmueble() != null) {
            vigencia.setNumeroInmueble(dto.getNumeroInmueble());
        }
        if (dto.getFechaRefPago() != null) {
            vigencia.setFechaRefPago(dto.getFechaRefPago());
        }
        if (dto.getReferenciaPagos() != null) {
            vigencia.setReferenciaPagos(dto.getReferenciaPagos());
        }
        if (dto.getAvaluoCatastral() != null) {
            vigencia.setAvaluoCatastral(dto.getAvaluoCatastral());
        }
        if (dto.getImpuestoPredial() != null) {
            vigencia.setImpuestoPredial(dto.getImpuestoPredial());
        }
        if (dto.getInteresesPredial() != null) {
            vigencia.setInteresesPredial(dto.getInteresesPredial());
        }
        if (dto.getSac() != null) {
            vigencia.setSac(dto.getSac());
        }
        if (dto.getInteresesSac() != null) {
            vigencia.setInteresesSac(dto.getInteresesSac());
        }
        if (dto.getDescuentos() != null) {
            vigencia.setDescuentos(dto.getDescuentos());
        }
        if (dto.getSobretasaAmbiental() != null) {
            vigencia.setSobretasaAmbiental(dto.getSobretasaAmbiental());
        }
        if (dto.getTarifas() != null) {
            vigencia.setTarifas(dto.getTarifas());
        }
        
        vigencia.calcularTotal();
    }

    private VigenciaResponseDTO toResponseDTO(PredioVigencia v) {
        VigenciaResponseDTO dto = new VigenciaResponseDTO();
        dto.setId(v.getId());
        dto.setPrediovId(v.getPrediovId());
        dto.setVigencia(v.getVigencia());
        dto.setNumeroInmueble(v.getNumeroInmueble());
        dto.setFechaRefPago(v.getFechaRefPago());
        dto.setReferenciaPagos(v.getReferenciaPagos());
        dto.setAvaluoCatastral(v.getAvaluoCatastral());
        dto.setImpuestoPredial(v.getImpuestoPredial());
        dto.setInteresesPredial(v.getInteresesPredial());
        dto.setSac(v.getSac());
        dto.setInteresesSac(v.getInteresesSac());
        dto.setDescuentos(v.getDescuentos());
        dto.setSobretasaAmbiental(v.getSobretasaAmbiental());
        dto.setTarifas(v.getTarifas());
        dto.setTotal(v.getTotal());
        dto.setEstado(v.getEstado());
        dto.setCreatedAt(v.getCreatedAt());
        dto.setUpdatedAt(v.getUpdatedAt());
        return dto;
    }
}