package com.example.Multi_Tenant_Task_Management_System.dto;

import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;

public record UserDto(String username, String email, String password, String role, Integer tenantId) {

}
