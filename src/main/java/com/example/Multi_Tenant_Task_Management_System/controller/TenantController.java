package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.dto.ResponseWithTime;
import com.example.Multi_Tenant_Task_Management_System.dto.TenantDto;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.services.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }


    // Endpoint to retrieve the CSRF token
    @GetMapping("/csrf-token")
    @ResponseBody
    public CsrfToken getCsrf(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    // Endpoint to display the form for creating a new tenant
    @GetMapping("/new")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String createTenantForm(Model model) {

        return "createTenant";  // Return the view name for creating a tenant
    }

    // Endpoint to create a new tenant
    @PostMapping
    @PreAuthorize("hasRole('SuperAdmin')")
    public String createTenant(@ModelAttribute TenantDto tenantDto, Model model) {
        tenantService.createTenant(tenantDto); // Call the service to create the tenant
        return "redirect:/tenants";  // Redirect to the tenants list after creation
    }

    // Endpoint to retrieve all tenants (JSON response)
    @GetMapping
    @ResponseBody
    public ResponseWithTime<List<Tenant>> showAllTenants() {
        return tenantService.findAllTenants();
    }

    // Endpoint to retrieve a specific tenant by ID (JSON response)
    @GetMapping("/{tenant_id}")
    @ResponseBody
    public ResponseWithTime<Tenant> getTenant(@PathVariable("tenant_id") Integer tenantId) {
        return tenantService.getTenant(tenantId);
    }

    // Display form to update a specific tenant by ID
    @GetMapping("/{tenant_id}/update")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String getTenantForUpdate(@PathVariable("tenant_id") Integer tenantId, Model model) {
        Tenant tenant = tenantService.getTenant(tenantId).getData();
        TenantDto tenantDto = TenantDto.fromTenant(tenant); // Convert to TenantDto without ID
        model.addAttribute("tenant", tenantDto);            // Add tenant data (without ID)
        model.addAttribute("tenant_id", tenantId);          // Pass tenant ID separately
        return "updateTenant";                              // Render updateTenant form
    }

    // Handle form submission to update a tenant
    @PostMapping("/{tenant_id}/update")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String updateTenant(@PathVariable("tenant_id") Integer tenantId, @ModelAttribute TenantDto tenantDto) {
        tenantService.updateTenant(tenantId, tenantDto);  // Update tenant using ID from path variable
        return "redirect:/tenants";                       // Redirect to the tenants list after update
    }

    // Endpoint to inactivate a specific tenant by ID (JSON response)
    // Display the form for inactivating a tenant
    @GetMapping("/inactivate")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String inactivateTenantForm() {
        return "inactivateTenant";  // Return the inactivate tenant form view
    }

    // Handle the form submission to inactivate a tenant
    @PostMapping("/inactivate")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String inactivateTenant(@RequestParam("tenantId") Integer tenantId, Model model) {
        ResponseWithTime<String> response = tenantService.inactivateTenant(tenantId);
        model.addAttribute("response", response.getData());  // Add response message to model
        return "redirect:/tenants";  // Redirect to the tenants list after inactivation
    }

    // Display form for deleting a tenant
    @GetMapping("/delete")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String deleteTenantForm() {
        return "deleteTenant";  // Render the deleteTenant.html form
    }

    // Handle form submission to delete a tenant
    @PostMapping("/delete")
    @PreAuthorize("hasRole('SuperAdmin')")
    public String deleteTenant(@RequestParam("tenantId") Integer tenantId) {
        tenantService.deleteTenant(tenantId);  // Call the service to delete the tenant by ID
        return "redirect:/tenants";            // Redirect to tenants list after deletion
    }
}