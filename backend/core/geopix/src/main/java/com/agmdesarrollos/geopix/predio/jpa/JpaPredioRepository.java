package com.agmdesarrollos.geopix.predio.jpa;

import com.agmdesarrollos.geopix.predio.domain.Predio;
import com.agmdesarrollos.geopix.predio.domain.Titular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPredioRepository extends JpaRepository<Predio, String> {

    Optional<Predio> findById(String id);

    boolean existsById(String id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Predio p WHERE p.datosBasicos.matriculaInmobiliaria = :matricula")
    boolean existsByMatriculaInmobiliaria(@Param("matricula") String matricula);

    @Query("SELECT p FROM Predio p WHERE p.datosBasicos.matriculaInmobiliaria = :matricula AND p.id <> :excludeId")
    Optional<Predio> findByMatriculaExcludingId(@Param("matricula") String matricula, @Param("excludeId") String excludeId);

    @Query("SELECT t FROM Titular t WHERE t.prediovId = :predioId")
    List<Titular> findTitularesByPredioId(@Param("predioId") String predicadoId);
}