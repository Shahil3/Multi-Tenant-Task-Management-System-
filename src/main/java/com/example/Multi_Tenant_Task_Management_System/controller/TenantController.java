package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.Tenant;

import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TenantController {


    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService service){
        this.tenantService = service;
    }

    @GetMapping("/")
    public String Greet(){
        return "Hello!!!";
    }

    @PostMapping("/tenants")
    public void createTenant(@RequestBody TenantDto tenantDto){
           tenantService.createTenant(tenantDto);
    }

    @GetMapping("/tenants")
    public List<Tenant>showAllTenants(){
        return tenantService.findAllTenants();
    }
}
