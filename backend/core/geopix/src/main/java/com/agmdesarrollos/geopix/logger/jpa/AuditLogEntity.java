package com.agmdesarrollos.geopix.logger.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Registro inmutable de cada cambio realizado en el sistema.
 * Almacena quién realizó la acción, qué entidad fue afectada,
 * el tipo de operación y un snapshot de los valores antes y después.
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entityName, entityId"),
        @Index(name = "idx_audit_performed_by", columnList = "performedBy"),
        @Index(name = "idx_audit_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nombre de la entidad afectada (e.g. "USER", "ROLE").
     */
    @Column(nullable = false, length = 50)
    private String entityName;

    /**
     * ID de la entidad afectada.
     */
    @Column(nullable = false)
    private String entityId;

    /**
     * Tipo de operación realizada.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditAction action;

    /**
     * Descripción legible del cambio realizado.
     */
    @Column(nullable = false, length = 500)
    private String description;

    /**
     * Snapshot JSON de los valores anteriores (null en CREATE).
     */
    @Column(columnDefinition = "TEXT")
    private String previousValues;

    /**
     * Snapshot JSON de los valores nuevos (null en DELETE).
     */
    @Column(columnDefinition = "TEXT")
    private String newValues;

    /**
     * Username del usuario que realizó la acción.
     */
    @Column(nullable = false)
    private String performedBy;

    /**
     * Timestamp del momento en que se realizó la acción.
     */
    @Column(nullable = false)
    private Instant timestamp;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = Instant.now();
        }
    }

    public enum AuditAction {
        CREATE,
        UPDATE,
        DELETE,
        RESTORE,
        ENABLE,
        DISABLE
    }
}
