package com.agmdesarrollos.geopix.features.wizard.persistance;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "borradores")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Borrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "predio_id")
    Predio predio;

    Integer step;
}
