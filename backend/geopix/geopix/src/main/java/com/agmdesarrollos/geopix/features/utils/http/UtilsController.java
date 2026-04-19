package com.agmdesarrollos.geopix.features.utils.http;

import com.agmdesarrollos.geopix.features.utils.http.dtos.DepartmentResponse;
import com.agmdesarrollos.geopix.features.utils.http.dtos.MunicipalityResponse;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.DepartamentoRepository;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.MunicipioRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")

@PermitAll
public class UtilsController {
    private final DepartamentoRepository departamentoRepository;
    private final MunicipioRepository municipioRepository;

    public UtilsController(DepartamentoRepository departamentoRepository, MunicipioRepository municipioRepository) {
        this.departamentoRepository = departamentoRepository;
        this.municipioRepository = municipioRepository;
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> retrieveDepartments() {
        List<DepartmentResponse> departments = departamentoRepository.findAll().stream()
                .map(dept -> new DepartmentResponse(dept.getId(), dept.getName()))
                .toList(); // Modern Java (16+) shorthand for collect(Collectors.toList())

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}/municipalities")
    public ResponseEntity<List<MunicipalityResponse>> retrieveMunicipalities(@PathVariable Long id) {
        List<MunicipalityResponse> municipalities = municipioRepository.findByDepartamentoId(id).stream()
                .map(municipio -> new MunicipalityResponse(municipio.getId(), municipio.getName()))
                .toList();
        return ResponseEntity.ok(municipalities);
    }
}
