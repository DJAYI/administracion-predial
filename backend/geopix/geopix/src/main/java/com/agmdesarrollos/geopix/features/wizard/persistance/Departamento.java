package com.agmdesarrollos.geopix.features.wizard.persistance;

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

    String name;

    @OneToMany
    Set<Municipio> municipios;
}
