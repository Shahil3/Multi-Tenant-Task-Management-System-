package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.Tenant;
import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {


    private final TenantRepository tenantRepo;
    public TenantService(TenantRepository repo){
        this.tenantRepo = repo;
    }

    public void createTenant(TenantDto tenantDto){
        tenantRepo.save(tenantDto.toTenant());
    }
    public List<Tenant>findAllTenants(){
        return tenantRepo.findAll();
    }
}
