package com.example.Multi_Tenant_Task_Management_System.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> viewLinks = new ArrayList<>();
        List<String> actionLinks = new ArrayList<>();

        boolean isSuperAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SuperAdmin"));
        boolean isTenantAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_TenantAdmin"));

        if (isSuperAdmin) {
            model.addAttribute("role", "SuperAdmin");

            // Viewing Links for SuperAdmin
            viewLinks.add("/tenants");  // View all tenants
            viewLinks.add("/tenants/{id}");  // View specific tenant details

            // Action Links for SuperAdmin
            actionLinks.add("/tenants/new");  // Create new tenant
            actionLinks.add("/tenants/{id}/update");  // Update tenant
            actionLinks.add("/tenants/delete");  // Delete tenant
            actionLinks.add("/tenants/{id}/inactivate");  // Inactivate tenant
        }

        if (isTenantAdmin) {
            model.addAttribute("role", "TenantAdmin");

            // Viewing Links for TenantAdmin
            viewLinks.add("/tenants/{id}");  // View own tenant details
            viewLinks.add("/users");
            // Action Links for TenantAdmin
            actionLinks.add("/tenants/{id}/update");  // Update own tenant details
            actionLinks.add("/tasks");  // View tasks (assuming a TenantAdmin might manage tasks)
            actionLinks.add("/tasks/new");  // Create new tasks for their own tenant
        }

        model.addAttribute("viewLinks", viewLinks);
        model.addAttribute("actionLinks", actionLinks);

        return "home";
    }
}