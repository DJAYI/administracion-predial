package com.agmdesarrollos.geopix.logger;

import com.agmdesarrollos.geopix.logger.jpa.AuditLogEntity;
import com.agmdesarrollos.geopix.logger.jpa.AuditLogEntity.AuditAction;
import com.agmdesarrollos.geopix.logger.jpa.JpaAuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final JpaAuditLogRepository auditLogRepository;

    public AuditLogService(JpaAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Registra una acción en el log de auditoría.
     * Se ejecuta en una transacción separada para que el registro
     * no se pierda si la transacción principal falla.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName,
                    String entityId,
                    AuditAction action,
                    String description,
                    String previousValues,
                    String newValues) {

        String performedBy = getCurrentUsername();

        AuditLogEntity entry = new AuditLogEntity();
        entry.setEntityName(entityName);
        entry.setEntityId(entityId);
        entry.setAction(action);
        entry.setDescription(description);
        entry.setPreviousValues(previousValues);
        entry.setNewValues(newValues);
        entry.setPerformedBy(performedBy);
        entry.setTimestamp(Instant.now());

        auditLogRepository.save(entry);

        log.info("[AUDIT] {} {} {} by {} — {}",
                action, entityName, entityId, performedBy, description);
    }

    /**
     * Sobrecarga simplificada sin snapshots de valores.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName,
                    String entityId,
                    AuditAction action,
                    String description) {
        log(entityName, entityId, action, description, null, null);
    }

    /**
     * Obtiene el historial de cambios de una entidad específica.
     */
    @Transactional(readOnly = true)
    public List<AuditLogEntity> getEntityHistory(String entityName, String entityId) {
        return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId);
    }

    /**
     * Obtiene todas las acciones realizadas por un usuario.
     */
    @Transactional(readOnly = true)
    public List<AuditLogEntity> getActionsByUser(String username) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(username);
    }

    /**
     * Obtiene todo el historial de auditoría de un tipo de entidad.
     */
    @Transactional(readOnly = true)
    public List<AuditLogEntity> getAllByEntity(String entityName) {
        return auditLogRepository.findByEntityNameOrderByTimestampDesc(entityName);
    }

    // ── Internal ─────────────────────────────────────────────────────────────

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
