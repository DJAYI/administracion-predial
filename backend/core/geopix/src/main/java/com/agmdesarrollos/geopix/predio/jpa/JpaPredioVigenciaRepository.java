package com.agmdesarrollos.geopix.predio.jpa;

import com.agmdesarrollos.geopix.predio.domain.PredioVigencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPredioVigenciaRepository extends JpaRepository<PredioVigencia, Long> {
    List<PredioVigencia> findByPrediovId(String prediovId);
    List<PredioVigencia> findByPrediovIdOrderByVigenciaDesc(String prediovId);
    void deleteByPrediovId(String prediovId);
}