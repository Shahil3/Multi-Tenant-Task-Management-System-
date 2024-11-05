package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.services.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TenantController {


    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService service) {
        this.tenantService = service;
    }

    @GetMapping("/")
    public String Greet(HttpServletRequest request) {
        return "Hello!!!" +"\n" + "Session Id : " + request.getSession().getId();
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrf(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/tenants")
    public void createTenant(@RequestBody TenantDto tenantDto) {
        tenantService.createTenant(tenantDto);
    }

    @GetMapping("/tenants")
    public List<Tenant> showAllTenants() {
        return tenantService.findAllTenants();
    }

    @GetMapping("/tenants/{tenant_id}")
    public Tenant GetTenant(@PathVariable Integer tenant_id) {
        return tenantService.getTenant(tenant_id);
    }
    @PostMapping("/inactivate/tenants/{tenant_id}")
    public String inactivate_tenant(@PathVariable Integer tenant_id){
        return tenantService.inactivateTenant(tenant_id);
    }
}
