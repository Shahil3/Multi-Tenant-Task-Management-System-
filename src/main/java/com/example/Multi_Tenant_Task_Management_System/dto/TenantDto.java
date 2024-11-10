package com.example.Multi_Tenant_Task_Management_System.dto;

import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;

public record TenantDto(String name, String description) {
    public Tenant toTenant() {
        return new Tenant(name, description);
    }

    public static TenantDto fromTenant(Tenant tenant) {
        return new TenantDto(tenant.getName(), tenant.getDescription());
    }
}
