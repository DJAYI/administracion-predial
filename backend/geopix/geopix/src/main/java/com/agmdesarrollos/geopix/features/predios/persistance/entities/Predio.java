package com.agmdesarrollos.geopix.features.predios.persistance.entities;

import com.agmdesarrollos.geopix.features.wizard.persistance.Departamento;
import com.agmdesarrollos.geopix.features.wizard.persistance.Municipio;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "predios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nationalPropertyNumber"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Predio {

    // --- CONSTANTES (Para eliminar Magic Strings) ---
    public static final String DEFAULT_NO_INFO = "SIN INFORMACION";
    public static final String OP_SI = "SI";
    public static final String AREA_SUFFIX = ".00 m²";
    private static final int HA_TO_M2_FACTOR = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // --- DATOS BÁSICOS ---

    @NotBlank(message = "Matrícula inmobiliaria es obligatoria")
    @Column(length = 30, nullable = false)
    String realStateRegistration;

    @Column(length = 30, unique = true)
    String nationalPropertyNumber;

    @Column(length = 30)
    String cadastralReference = DEFAULT_NO_INFO;

    @Column(length = 30)
    String nationalPropertyNumberGEO = DEFAULT_NO_INFO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Departamento department;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipalities_id")
    Municipio municipalities;

    @NotEmpty
    @ManyToMany
    @JoinTable(name = "predio_condiciones")
    List<TipoPredio> propertyConditions;

    @Column(nullable = false)
    String hasEasement; // Valores: SI, NO, SIN INFORMACIÓN

    @ManyToOne
    @JoinColumn(name = "easement_type_id")
    TipoServidumbre easementType;

    @ManyToOne
    @JoinColumn(name = "soil_type_id")
    TipoSuelo soilType;

    @ManyToOne
    @JoinColumn(name = "destiny_type_id")
    TipoDestino destinyType;

    @ManyToOne
    @JoinColumn(name = "tipo_titularidad_id")
    TipoTitularidad tipoTitularidad;

    @ManyToOne
    @JoinColumn(name = "expediente_id")
    TipoExpediente expediente;

    @NotBlank
    @Column(length = 255)
    String address;

    @NotBlank
    @Column(length = 255)
    String alias;

    @NotNull
    Boolean environmentalImpact;

    @Column(length = 300)
    String observations;

    // --- DATOS FÍSICOS ---

    Integer areaVurHa;
    Integer areaVurM2;
    String areaVurTotal;

    Integer areaCatastroHa;
    Integer areaCatastroM2;
    String areaCatastroTotal;

    Integer areaEscriturasHa;
    Integer areaEscriturasM2;
    String areaEscriturasTotal;

    Integer areaMedicionHa;
    Integer areaMedicionM2;
    String areaMedicionTotal;

    @Column(length = 500)
    String cabidaLinderos;

    // --- LÓGICA DE NEGOCIO Y CICLO DE VIDA ---

    @PrePersist
    @PreUpdate
    public void validateAndCalculate() {
        // Manejo de valores nulos o vacíos para referencias
        this.cadastralReference = sanitizeString(this.cadastralReference);
        this.nationalPropertyNumberGEO = sanitizeString(this.nationalPropertyNumberGEO);

        // Validación RG_01: Servidumbre
        if (OP_SI.equalsIgnoreCase(this.hasEasement) && this.easementType == null) {
            throw new IllegalStateException("Cuando existe servidumbre, el campo Tipo de Servidumbre no puede estar vacío.");
        }

        // Cálculos de áreas físicas
        this.areaVurTotal = formatArea(this.areaVurHa, this.areaVurM2);
        this.areaCatastroTotal = formatArea(this.areaCatastroHa, this.areaCatastroM2);
        this.areaEscriturasTotal = formatArea(this.areaEscriturasHa, this.areaEscriturasM2);
        this.areaMedicionTotal = formatArea(this.areaMedicionHa, this.areaMedicionM2);
    }

    private String sanitizeString(String value) {
        return (value == null || value.trim().isEmpty()) ? DEFAULT_NO_INFO : value;
    }

    private String formatArea(Integer ha, Integer m2) {
        if (ha == null || m2 == null) {
            return DEFAULT_NO_INFO;
        }
        long totalM2 = ((long) ha * HA_TO_M2_FACTOR) + m2;
        return totalM2 + AREA_SUFFIX;
    }
}