package com.agmdesarrollos.geopix.logger.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {

    /**
     * Find all audit entries for a specific entity, ordered by most recent first.
     */
    List<AuditLogEntity> findByEntityNameAndEntityIdOrderByTimestampDesc(String entityName, String entityId);

    /**
     * Find all audit entries performed by a specific user.
     */
    List<AuditLogEntity> findByPerformedByOrderByTimestampDesc(String performedBy);

    /**
     * Find all audit entries for a specific entity type.
     */
    List<AuditLogEntity> findByEntityNameOrderByTimestampDesc(String entityName);
}
