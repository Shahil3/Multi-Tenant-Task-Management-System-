package com.example.Multi_Tenant_Task_Management_System.repository;

import com.example.Multi_Tenant_Task_Management_System.entity.Project;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
}
