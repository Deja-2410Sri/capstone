package com.cropadvisory.platform.service;

import com.cropadvisory.platform.model.entity.AuditLog;
import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for audit logging of important platform actions.
 */
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(User user, String action, String entityType, Long entityId, String description, String ipAddress) {
        AuditLog auditLog = AuditLog.builder()
                .user(user).action(action).entityType(entityType)
                .entityId(entityId).description(description)
                .ipAddress(ipAddress).build();
        auditLogRepository.save(auditLog);
    }

    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<AuditLog> getAuditLogsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}
