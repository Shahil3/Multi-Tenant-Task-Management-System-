package com.example.Multi_Tenant_Task_Management_System.dto;

import com.example.Multi_Tenant_Task_Management_System.Tenant;

public record TenantDto(String name , String description) {
    public Tenant toTenant(){
        return new Tenant(name,description);
    }
}
