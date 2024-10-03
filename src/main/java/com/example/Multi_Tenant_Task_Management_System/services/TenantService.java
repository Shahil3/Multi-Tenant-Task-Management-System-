package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;

import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
    public void createTenant(@NotNull TenantDto tenantDto) {
        tenantRepo.save(tenantDto.toTenant());
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    public List<Tenant> findAllTenants() {
        return tenantRepo.findAll();
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    public Tenant getTenant(Integer id) {
        Optional<Tenant> optionalTenant = tenantRepo.findById(id);
        if (optionalTenant.isPresent()) {
            return optionalTenant.get();
        } else throw new EntityNotFoundException("Tenant with id " + id + " not found");
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    public void updateTenant(Integer id, TenantDto tenantDto) {
        Optional<Tenant> optionalTenant = tenantRepo.findById(id);

        if (optionalTenant.isPresent()) {
            Tenant tenant = optionalTenant.get();
            tenant.setName(tenantDto.name());
            tenant.setDescription(tenantDto.description());
            tenant.setUpdatedAt(LocalDateTime.now());

            tenantRepo.save(tenant); // Save the updated tenant back to the repository
        } else {
            throw new EntityNotFoundException("Tenant with id " + id + " not found");
        }
    }

    @PreAuthorize("hasRole('SuperAdmin')")
    public void deleteTenant(Integer id) {
        tenantRepo.deleteById(id);
    }
}
