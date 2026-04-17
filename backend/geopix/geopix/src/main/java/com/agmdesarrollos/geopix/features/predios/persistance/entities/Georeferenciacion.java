package com.agmdesarrollos.geopix.features.predios.persistance.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "georeferenciaciones")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Georeferenciacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
