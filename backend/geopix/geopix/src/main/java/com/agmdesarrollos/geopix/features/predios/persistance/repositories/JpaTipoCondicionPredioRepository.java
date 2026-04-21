package com.agmdesarrollos.geopix.features.predios.persistance.repositories;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoCondicionPredio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTipoCondicionPredioRepository extends JpaRepository<TipoCondicionPredio, Long>, JpaSpecificationExecutor<TipoCondicionPredio> {
}
