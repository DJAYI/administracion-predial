package com.agmdesarrollos.geopix.features.predios.persistance.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "tipos_condiciones_predios")
public class TipoCondicionPredio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
}
