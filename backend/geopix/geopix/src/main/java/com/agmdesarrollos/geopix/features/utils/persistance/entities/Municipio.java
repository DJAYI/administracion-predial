package com.agmdesarrollos.geopix.features.utils.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "municipios")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String code_mun;

    @Column(unique = true, nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    Departamento departamento;
}
