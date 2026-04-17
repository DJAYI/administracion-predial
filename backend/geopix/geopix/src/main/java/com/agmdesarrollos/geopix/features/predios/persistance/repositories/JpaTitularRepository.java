package com.agmdesarrollos.geopix.features.predios.persistance.repositories;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.Titular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTitularRepository extends JpaRepository<Titular, Long> {
}
