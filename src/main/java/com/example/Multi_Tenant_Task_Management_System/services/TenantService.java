package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.dto.ResponseWithTime;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;

import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository tenantRepo;

    public TenantService(TenantRepository repo) {
        this.tenantRepo = repo;
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @CacheEvict(value = "tenants", allEntries = true)
    @Transactional
    public ResponseWithTime<String> createTenant(@NotNull TenantDto tenantDto) {
        long startTime = System.currentTimeMillis();

        tenantRepo.save(tenantDto.toTenant());

        long executionTime = System.currentTimeMillis() - startTime;
        return new ResponseWithTime<>("Tenant created successfully", executionTime);
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @Cacheable(value = "tenants")
    public ResponseWithTime<List<Tenant>> findAllTenants() {
        long startTime = System.currentTimeMillis();

        List<Tenant> tenants = tenantRepo.findAll();

        long executionTime = System.currentTimeMillis() - startTime;
        return new ResponseWithTime<>(tenants, executionTime);
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @Cacheable(value = "tenants", key = "#id")
    public ResponseWithTime<Tenant> getTenant(Integer id) {
        long startTime = System.currentTimeMillis();

        Tenant tenant = tenantRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id " + id + " not found"));

        long executionTime = System.currentTimeMillis() - startTime;
        return new ResponseWithTime<>(tenant, executionTime);
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @CacheEvict(value = "tenants", allEntries = true)
    @Transactional
    public ResponseWithTime<String> updateTenant(Integer id, TenantDto tenantDto) {
        long startTime = System.currentTimeMillis();

        Tenant tenant = tenantRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with id " + id + " not found"));
        tenant.setName(tenantDto.name());
        tenant.setDescription(tenantDto.description());
        tenant.setUpdatedAt(LocalDateTime.now());

        tenantRepo.save(tenant); // Save the updated tenant back to the repository

        long executionTime = System.currentTimeMillis() - startTime;
        return new ResponseWithTime<>("Tenant updated successfully", executionTime);
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @CacheEvict(value = "tenants", allEntries = true)
    @Transactional
    public ResponseWithTime<String> deleteTenant(Integer id) {
        long startTime = System.currentTimeMillis();

        tenantRepo.deleteById(id);

        long executionTime = System.currentTimeMillis() - startTime;
        return new ResponseWithTime<>("Tenant deleted successfully", executionTime);
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    @CacheEvict(value = "tenants", allEntries = true)
    @Transactional
    public ResponseWithTime<String> inactivateTenant(Integer tenantId) {
        long startTime = System.currentTimeMillis();

        Optional<Tenant> tenant = tenantRepo.findById(tenantId);
        if (tenant.isPresent()) {
            tenant.get().setActive(false);
            tenantRepo.save(tenant.get()); // Save the updated tenant back to the repository
            long executionTime = System.currentTimeMillis() - startTime;
            return new ResponseWithTime<>("Tenant inactivated successfully", executionTime);
        } else {
            long executionTime = System.currentTimeMillis() - startTime;
            return new ResponseWithTime<>("No Tenant with id: " + tenantId + " exists", executionTime);
        }
    }
}