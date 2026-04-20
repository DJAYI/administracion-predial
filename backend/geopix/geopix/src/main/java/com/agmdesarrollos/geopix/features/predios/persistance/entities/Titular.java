package com.agmdesarrollos.geopix.features.predios.persistance.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "titulares", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document_type", "document_number"}) // RNG-07
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Titular {

    public enum DocumentType { CC, NIT, CE, PP, PPT }

    // RNG_01: Tipos de titularidad para controlar visibilidad
    public enum TitularityType { PROMESA, FIDEICOMISO, PROPIETARIO, INFORMALIDAD }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "titularity_type", nullable = false)
    TitularityType titularidadType;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    DocumentType documentType;

    @Column(name = "document_number", nullable = false)
    String documentNumber;

    // RNG-08 y RNG_04: El porcentaje debe ser > 0 y <= 100
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.0")
    @Column(name = "right_percent", nullable = false)
    Double rightPercent;

    // Campos para Persona Natural (Se habilitan si documentType != NIT)
    String firstName;
    String middleName;
    String firstSurname;
    String secondSurname;

    // Campo para Persona Jurídica (Se habilita si documentType == NIT)
    String socialReason;

    // --- Campos de Soporte de Titularidad ---

    String deedNumber;      // RNG-09: Obligatorio si existe deedDate

    LocalDate deedDate;  // Fecha Escritura

    LocalDate annotationDate; // Fecha Anotación (RNG_05: >= deedDate)

    LocalDate promiseDate;    // Fecha Promesa (RNG_01)

    String trustCode;             // Código Fideicomiso (RNG-11)

    String notary;                // RNG-10: Obligatorio si existe deedNumber

    @Column(columnDefinition = "TEXT")
    String observations;          // RNG_01: Para tipo INFORMALIDAD

    // Relación con Predio (RNG-06: Un predio puede tener múltiples titulares)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predio_id")
    Predio predio;
}