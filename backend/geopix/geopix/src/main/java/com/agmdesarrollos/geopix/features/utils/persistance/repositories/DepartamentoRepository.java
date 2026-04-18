package com.agmdesarrollos.geopix.features.utils.persistance.repositories;

import com.agmdesarrollos.geopix.features.utils.persistance.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByName(String name);
}