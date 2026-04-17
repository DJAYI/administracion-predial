package com.agmdesarrollos.geopix.features.predios.persistance.repositories;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.TipoExpediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTipoExpedienteRepository extends JpaRepository<TipoExpediente, Long> {
}
