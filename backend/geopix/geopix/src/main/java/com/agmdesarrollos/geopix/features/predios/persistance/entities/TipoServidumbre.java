package com.agmdesarrollos.geopix.features.predios.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "tipos_servidumbre")
@SQLDelete(sql = "UPDATE tipos_servidumbre SET deleted = true WHERE id=?")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TipoServidumbre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    @Column(nullable = false)
    Boolean deleted = false;
}
