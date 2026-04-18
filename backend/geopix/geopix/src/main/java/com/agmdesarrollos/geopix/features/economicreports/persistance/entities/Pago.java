package com.agmdesarrollos.geopix.features.economicreports.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "pagos")

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pago {

    public enum PagoTarget {
        GENERAL, VIGENCIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Double amountPaid;

    @Column(nullable = false)
    LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    PagoTarget applyTo; // GENERAL or VIGENCIA

    String infoSource;
    String destination;

    @Column(columnDefinition = "TEXT")
    String observations;

    String attachmentUrl; // Support file path

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deuda_id")
    Deuda deuda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vigencia_id")
    Vigencia vigenciaDestino; // Null if applyTo is GENERAL
}
