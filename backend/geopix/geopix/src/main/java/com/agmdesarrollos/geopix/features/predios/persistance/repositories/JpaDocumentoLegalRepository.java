package com.agmdesarrollos.geopix.features.predios.persistance.repositories;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.DocumentoLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDocumentoLegalRepository extends JpaRepository<DocumentoLegal, Long> {
}
