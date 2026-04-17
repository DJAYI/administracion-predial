package com.agmdesarrollos.geopix.features.economicreports.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "deudas")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Deuda {

    enum DebtStatus { CURRENT, OVERDUE, DELINQUENT, IN_COLLECTION, SEIZED, FORECLOSED, PRESCRIBED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double debtTotal;
    Double totalPaid;

    @OneToMany
    List<Vigencia> vigencias;


}
