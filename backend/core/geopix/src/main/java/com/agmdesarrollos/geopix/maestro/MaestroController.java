package com.agmdesarrollos.geopix.maestro;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/maestros")
public class MaestroController {

    @GetMapping("/tipo-predio")
    public List<MaestroOption> getTipoPredio() {
        return List.of(
            new MaestroOption("NPH", "NPH - Predio No Homologado"),
            new MaestroOption("PH_MATRIZ", "PH Matriz - Propiedad Horizontal Matriz"),
            new MaestroOption("PH_UNIDAD_PREDIAL", "PH Unidad Predial"),
            new MaestroOption("CONDOMINIO_MATRIZ", "Condominio Matriz"),
            new MaestroOption("DOMINIO_UNIDAD_PREDIAL", "Dominio Unidad Predial"),
            new MaestroOption("PARQUE_CEMENTERIO_MATRIZ", "Parque Cementerio Matriz"),
            new MaestroOption("PARQUE_CEMENTERIO_UNIDAD_PREDIAL", "Parque Cementerio Unidad Predial"),
            new MaestroOption("VIA", "Via"),
            new MaestroOption("INFORMAL", "Informal"),
            new MaestroOption("BIEN_USO_PUBLICO", "Bien Uso Público")
        );
    }

    @GetMapping("/destino")
    public List<MaestroOption> getDestino() {
        return List.of(
            new MaestroOption("HABITACIONAL", "Habitacional"),
            new MaestroOption("COMERCIAL", "Comercial"),
            new MaestroOption("INDUSTRIAL", "Industrial"),
            new MaestroOption("INSTITUCIONAL", "Institucional"),
            new MaestroOption("AGROPECUARIO", "Agropecuario"),
            new MaestroOption("RECREACIONAL", "Recreacional"),
            new MaestroOption("MIXTO", "Mixto")
        );
    }

    @GetMapping("/tipo-suelo")
    public List<MaestroOption> getTipoSuelo() {
        return List.of(
            new MaestroOption("URBANO", "Urbano"),
            new MaestroOption("RURAL", "Rural"),
            new MaestroOption("EXPANSION", "Expansión")
        );
    }

    @GetMapping("/servidumbre")
    public List<MaestroOption> getServidumbre() {
        return List.of(
            new MaestroOption("SI", "Sí"),
            new MaestroOption("NO", "No"),
            new MaestroOption("SIN_INFORMACION", "Sin Información")
        );
    }

    @GetMapping("/tipo-servidumbre")
    public List<MaestroOption> getTipoServidumbre() {
        return List.of(
            new MaestroOption("TRANSITO", "Servidumbre de Tránsito o Paso"),
            new MaestroOption("ELECTRICA", "Servidumbre Eléctrica"),
            new MaestroOption("TELECOMUNICACION", "Servidumbre de Telecomunicaciones"),
            new MaestroOption("ACUEDUCTO", "Servidumbre de Acueducto"),
            new MaestroOption("OTRA", "Otra")
        );
    }

    @GetMapping("/afectacion")
    public List<MaestroOption> getAfectacion() {
        return List.of(
            new MaestroOption("SI", "Sí"),
            new MaestroOption("NO", "No"),
            new MaestroOption("SIN_INFORMACION", "Sin Información")
        );
    }

    @GetMapping("/expediente")
    public List<MaestroOption> getExpediente() {
        return List.of(
            new MaestroOption("DIGITAL", "Digital"),
            new MaestroOption("FISICO", "Físico"),
            new MaestroOption("DIGITAL_Y_FISICO", "Digital y Físico"),
            new MaestroOption("SIN_INFORMACION", "Sin Información")
        );
    }

    @GetMapping("/tipo-documento")
    public List<MaestroOption> getTipoDocumento() {
        return List.of(
            new MaestroOption("CC", "Cédula de Ciudadanía"),
            new MaestroOption("CE", "Cédula de Extranjería"),
            new MaestroOption("NIT", "NIT (Número de Identificación Tributaria)"),
            new MaestroOption("PASAPORTE", "Pasaporte"),
            new MaestroOption("NIT_ENTIDAD", "NIT Entidad")
        );
    }

    @GetMapping("/tipo-titularidad")
    public List<MaestroOption> getTipoTitularidad() {
        return List.of(
            new MaestroOption("TITULAR", "Titular"),
            new MaestroOption("INFORMALIDAD", "Informalidad"),
            new MaestroOption("TENENCIA_POR_DEFINIR", "Tenencia por Definir"),
            new MaestroOption("POSEEDOR", "Poseedor"),
            new MaestroOption("ARRENDATARIO", "Arrendatario")
        );
    }

    @GetMapping("/estado-registro")
    public List<MaestroOption> getEstadoRegistro() {
        return List.of(
            new MaestroOption("BORRADOR", "Borrador"),
            new MaestroOption("COMPLETO", "Completo"),
            new MaestroOption("ACTIVO", "Activo"),
            new MaestroOption("INACTIVO", "Inactivo")
        );
    }

    @GetMapping("/estado-vigencia")
    public List<MaestroOption> getEstadoVigencia() {
        return List.of(
            new MaestroOption("EN_DEUDA", "En Deuda"),
            new MaestroOption("PAGADO", "Pagado"),
            new MaestroOption("PAGO_PARCIAL", "Pago Parcial")
        );
    }

    @GetMapping("/all")
    public Map<String, List<MaestroOption>> getAllMaestros() {
        Map<String, List<MaestroOption>> todos = new LinkedHashMap<>();
        todos.put("tipoPredio", getTipoPredio());
        todos.put("destino", getDestino());
        todos.put("tipoSuelo", getTipoSuelo());
        todos.put("servidumbre", getServidumbre());
        todos.put("tipoServidumbre", getTipoServidumbre());
        todos.put("afectacion", getAfectacion());
        todos.put("expediente", getExpediente());
        todos.put("tipoDocumento", getTipoDocumento());
        todos.put("tipoTitularidad", getTipoTitularidad());
        todos.put("estadoRegistro", getEstadoRegistro());
        todos.put("estadoVigencia", getEstadoVigencia());
        return todos;
    }

    public record MaestroOption(String value, String label) {}
}