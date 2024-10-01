package com.example.Multi_Tenant_Task_Management_System.repository;

import com.example.Multi_Tenant_Task_Management_System.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository  extends JpaRepository<Tenant,Integer> {
}
