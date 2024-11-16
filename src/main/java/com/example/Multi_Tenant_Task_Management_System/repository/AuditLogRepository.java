package com.example.Multi_Tenant_Task_Management_System.repository;

import com.example.Multi_Tenant_Task_Management_System.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}