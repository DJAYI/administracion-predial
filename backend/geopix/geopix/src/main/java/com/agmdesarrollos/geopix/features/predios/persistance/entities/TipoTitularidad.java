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
@Table(name = "tipos_titulares")
@SQLDelete(sql = "UPDATE tipos_titulares SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TipoTitularidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(nullable = false)
    boolean deleted = Boolean.FALSE;

}
