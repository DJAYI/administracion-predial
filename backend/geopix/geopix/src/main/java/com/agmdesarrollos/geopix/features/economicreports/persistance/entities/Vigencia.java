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
@Table(name = "vigencias")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vigencia {

    public enum VigenciaStatus {
        UP_TO_DATE, IN_DEBT, PAID, OVERDUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "year_label")
    Integer year;

    // Monetary breakdown (Financial Core)
    Double cadastralValue;
    Double propertyTax;
    Double propertyTaxInterest;

    @Column(name = "environmental_surcharge")
    Double environmentalSurcharge; // Refers to SAC

    @Column(name = "environmental_surcharge_interest")
    Double environmentalSurchargeInterest;

    Double discounts;

    @Column(name = "outstanding_balance")
    Double outstandingBalance; // What is currently owed for this year

    @Column(name = "total_debt")
    Double totalDebt; // Original total (Tax + Surcharges + Interests)

    @Enumerated(EnumType.STRING)
    VigenciaStatus status;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deuda_id")
    Deuda deuda;

    @OneToMany(mappedBy = "vigenciaDestino", cascade = CascadeType.ALL)
    List<Pago> targetedPayments;
}