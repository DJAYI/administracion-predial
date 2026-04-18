package com.agmdesarrollos.geopix.features.utils.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "departamentos")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    Set<Municipio> municipios;
}
