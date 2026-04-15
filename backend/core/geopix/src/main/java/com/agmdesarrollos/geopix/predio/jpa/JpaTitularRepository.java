package com.agmdesarrollos.geopix.predio.jpa;

import com.agmdesarrollos.geopix.predio.domain.Titular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTitularRepository extends JpaRepository<Titular, Long> {
    List<Titular> findByPrediovId(String prediovId);
    void deleteByPrediovId(String prediovId);
}