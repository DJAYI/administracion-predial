package com.agmdesarrollos.geopix.features.economicreports.persistance.entities;

import com.agmdesarrollos.geopix.features.predios.persistance.entities.Predio;
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

    enum DebtStatus { AL_DIA, SALDO_A_FAVOR, EN_DEUDA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String propertyNumber;

    Double totalBalance;      // Sum of all pending vigencias
    Double totalPaidToDate;
    Integer paymentCount;

    @Enumerated(EnumType.STRING)
    DebtStatus globalStatus;

    @OneToMany(mappedBy = "deuda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("year ASC") // Critical for "General" payment logic (oldest first)
    List<Vigencia> vigencias;

    @OneToMany(mappedBy = "deuda")
    List<Pago> paymentHistory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predio_id", referencedColumnName = "id")
    Predio predio;
}
