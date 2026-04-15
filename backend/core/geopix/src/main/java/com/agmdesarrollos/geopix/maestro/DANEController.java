package com.agmdesarrollos.geopix.maestro;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dane")
public class DANEController {

    private final CatalogosDANE catalogos;

    public DANEController(CatalogosDANE catalogos) {
        this.catalogos = catalogos;
    }

    @GetMapping("/departamentos")
    public List<CatalogosDANE.DepartamentoDTO> listarDepartamentos() {
        return catalogos.listarDepartamentos();
    }

    @GetMapping("/municipios/{codigoDepartamento}")
    public List<CatalogosDANE.MunicipioDTO> listarMunicipios(
            @PathVariable String codigoDepartamento) {
        return catalogos.listarMunicipios(codigoDepartamento);
    }
}