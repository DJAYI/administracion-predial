package com.agmdesarrollos.geopix.features.economicreports.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "pagos")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
