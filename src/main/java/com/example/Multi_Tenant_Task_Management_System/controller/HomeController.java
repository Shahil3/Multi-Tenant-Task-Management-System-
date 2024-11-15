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
        boolean isManager = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_Manager"));
        boolean isEmployee = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_Employee"));

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
            viewLinks.add("/users");  // View users

            // Action Links for TenantAdmin
            actionLinks.add("/tenants/{id}/update");  // Update own tenant details
            actionLinks.add("/tasks");  // View tasks
            actionLinks.add("/tasks/new");  // Create new tasks for their own tenant
        }

        // Common section for Manager and Employee roles
        if (isManager || isEmployee) {
            model.addAttribute("role", isManager ? "Manager" : "Employee");

            // Viewing Links for Manager and Employee
            viewLinks.add("/projects");  // View assigned projects
            viewLinks.add("/notifications");  // View notifications
            viewLinks.add("/comments");  // View comments

            if (isManager) {
                // Action Links for Manager
                actionLinks.add("/projects/assign/new");  // Assign project to an employee
                actionLinks.add("/notifications/new");  // Add a notification for an employee
                actionLinks.add("/comments/new");  // Add a comment for an employee
            }
        }

        model.addAttribute("viewLinks", viewLinks);
        model.addAttribute("actionLinks", actionLinks);

        return "home";
    }
}