package com.agmdesarrollos.geopix.features.utils.logic;

import com.agmdesarrollos.geopix.features.utils.logic.dtos.MunicipioRawDTO;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Departamento;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Municipio;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class GetDepartmentsAndMunicipalities {

    private final RestClient restClient;

    // Inyectamos el builder para aprovechar la autoconfiguración de Spring
    public GetDepartmentsAndMunicipalities(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://datos.gov.co")
                .build();
    }

    public List<Departamento> execute() {
        // 1. Realizar la petición HTTP y mapear al DTO
        List<MunicipioRawDTO> dtoList = restClient.get()
                .uri("/resource/82di-kkh9.json?$limit=1200")
                .retrieve()
                .body(new ParameterizedTypeReference<List<MunicipioRawDTO>>() {});

        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Procesar los DTOs para construir las Entidades JPA
        // Usamos un Map para evitar crear departamentos duplicados y agrupar los municipios
        Map<String, Departamento> departamentoMap = new HashMap<>();
        Map<String, Municipio> municipioProcessedMap = new HashMap<>(); // Track processed municipalities by name

        for (MunicipioRawDTO dto : dtoList) {

            Departamento departamento = departamentoMap.computeIfAbsent(dto.dpto(), key -> {
                Departamento newDepto = new Departamento();
                newDepto.setName(dto.dpto());
                newDepto.setMunicipios(new HashSet<>());
                return newDepto;
            });

            // If a municipality with this name was already processed, skip it to avoid unique constraint violation
            if (municipioProcessedMap.containsKey(dto.nomMpio())) {
                continue;
            }

            Municipio municipio = new Municipio();
            municipio.setCodeMun(dto.codMpio());
            municipio.setName(dto.nomMpio());
            municipio.setDepartamento(departamento);

            departamento.getMunicipios().add(municipio);
            municipioProcessedMap.put(dto.nomMpio(), municipio); // Mark as processed
        }

        // 3. Retornar la lista de entidades listas para ser persistidas
        return new ArrayList<>(departamentoMap.values());
    }
}