package com.agmdesarrollos.geopix.features.predios.logic;

import com.agmdesarrollos.geopix.features.predios.http.dtos.DatosBasicosPredioRequest;
import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import com.agmdesarrollos.geopix.features.predios.persistance.repositories.JpaPredioRepository;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.DepartamentoRepository;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.MunicipioRepository;
import org.springframework.stereotype.Service;

@Service
public class DatosBasicosPredioService {
    private final JpaPredioRepository predioRepository;
    private final MunicipioRepository municipioRepository;
    private final DepartamentoRepository departamentoRepository;

    public DatosBasicosPredioService(JpaPredioRepository predioRepository, MunicipioRepository municipioRepository, DepartamentoRepository departamentoRepository) {
        this.predioRepository = predioRepository;
        this.municipioRepository = municipioRepository;
        this.departamentoRepository = departamentoRepository;
    }

    public Predio addBasicData(DatosBasicosPredioRequest request) {
        Predio newPredio = new Predio();


    }
}
