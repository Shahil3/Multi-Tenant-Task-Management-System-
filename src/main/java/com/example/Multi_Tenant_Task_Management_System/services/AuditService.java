package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.entity.AuditLog;
import com.example.Multi_Tenant_Task_Management_System.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(Integer userId, Integer tenantId, String action, String entityName, Integer entityId, String oldValue, String newValue) {
        AuditLog auditLog = new AuditLog(userId, tenantId, action, entityName, entityId, oldValue, newValue);
        auditLogRepository.save(auditLog);
    }
}