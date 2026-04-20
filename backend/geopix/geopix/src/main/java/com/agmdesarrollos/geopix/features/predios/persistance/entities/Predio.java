package com.agmdesarrollos.geopix.features.predios.persistance.entities;

import com.agmdesarrollos.geopix.features.economicreports.persistance.entities.Deuda;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Departamento;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Municipio;
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
        @UniqueConstraint(columnNames = {"national_property_number"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Predio {

    // --- CONSTANTS ---
    public static final String DEFAULT_NO_INFO = "NO INFORMATION";
    public static final String OP_YES = "YES";
    public static final String AREA_SUFFIX = ".00 m²";
    private static final int HA_TO_M2_FACTOR = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    // --- BASIC DATA ---

    @NotBlank(message = "Real estate registration is mandatory")
    @Column(name = "real_estate_registration", length = 30, nullable = false)
    String realEstateRegistration;

    @Column(name = "national_property_number", length = 30, unique = true)
    String nationalPropertyNumber;

    @Column(name = "cadastral_reference", length = 30)
    String cadastralReference = DEFAULT_NO_INFO;

    @Column(name = "national_property_number_geo", length = 30)
    String nationalPropertyNumberGeo = DEFAULT_NO_INFO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Departamento department;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    Municipio municipality;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "property_conditions",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_type_id")
    )
    List<TipoCondicionPredio> propertyConditions;

    @Column(name = "has_easement", nullable = false)
    String hasEasement;

    @ManyToOne
    @JoinColumn(name = "easement_type_id")
    TipoServidumbre easementType;

    @ManyToOne
    @JoinColumn(name = "soil_type_id")
    TipoSuelo soilType;

    @ManyToOne
    @JoinColumn(name = "destination_type_id")
    TipoDestino destinationType;

    @ManyToOne
    @JoinColumn(name = "ownership_type_id")
    TipoTitularidad ownershipType;

    @ManyToOne
    @JoinColumn(name = "record_type_id")
    TipoExpediente recordType;

    @NotBlank
    @Column(name = "address", length = 255)
    String address;

    @NotBlank
    @Column(name = "alias", length = 255)
    String alias;

    @NotNull
    @Column(name = "environmental_impact")
    Boolean environmentalImpact;

    @Column(name = "observations", length = 300)
    String observations;

    // --- PHYSICAL DATA ---

    @Column(name = "vur_area_ha")
    Integer vurAreaHa;

    @Column(name = "vur_area_m2")
    Integer vurAreaM2;

    @Column(name = "vur_area_total")
    String vurAreaTotal;

    @Column(name = "cadastral_area_ha")
    Integer cadastralAreaHa;

    @Column(name = "cadastral_area_m2")
    Integer cadastralAreaM2;

    @Column(name = "cadastral_area_total")
    String cadastralAreaTotal;

    @Column(name = "deed_area_ha")
    Integer deedAreaHa;

    @Column(name = "deed_area_m2")
    Integer deedAreaM2;

    @Column(name = "deed_area_total")
    String deedAreaTotal;

    @Column(name = "measured_area_ha")
    Integer measuredAreaHa;

    @Column(name = "measured_area_m2")
    Integer measuredAreaM2;

    @Column(name = "measured_area_total")
    String measuredAreaTotal;

    @Column(name = "boundaries_and_dimensions", length = 500)
    String boundariesAndDimensions;

    // --- BUSINESS LOGIC & LIFECYCLE ---

    @PrePersist
    @PreUpdate
    public void validateAndCalculate() {
        // Handle null or empty values for references
        this.cadastralReference = sanitizeString(this.cadastralReference);
        this.nationalPropertyNumberGeo = sanitizeString(this.nationalPropertyNumberGeo);

        // Validation RG_01: Easement
        if (OP_YES.equalsIgnoreCase(this.hasEasement) && this.easementType == null) {
            throw new IllegalStateException("When an easement exists, the Easement Type field cannot be empty.");
        }

        // Physical area calculations
        this.vurAreaTotal = formatArea(this.vurAreaHa, this.vurAreaM2);
        this.cadastralAreaTotal = formatArea(this.cadastralAreaHa, this.cadastralAreaM2);
        this.deedAreaTotal = formatArea(this.deedAreaHa, this.deedAreaM2);
        this.measuredAreaTotal = formatArea(this.measuredAreaHa, this.measuredAreaM2);
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

    @OneToMany(mappedBy = "predio")
    List<Titular> owners;

    @OneToOne(mappedBy = "predio")
    Deuda debt;
}