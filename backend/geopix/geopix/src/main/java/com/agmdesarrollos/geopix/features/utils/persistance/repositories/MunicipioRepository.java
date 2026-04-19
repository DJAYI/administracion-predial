package com.agmdesarrollos.geopix.features.utils.persistance.repositories;
import com.agmdesarrollos.geopix.features.utils.http.dtos.MunicipalityResponse;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    // Para buscar por el código DANE
    Optional<Municipio> findByCodeMun(String codeMun);

    List<Municipio> findByDepartamentoId(Long id);
}
