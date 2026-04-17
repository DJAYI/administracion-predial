package com.agmdesarrollos.geopix.features.predios.persistance.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "tipos_predios")

@FieldDefaults(level = AccessLevel.PRIVATE)

public class TipoPredio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
}
