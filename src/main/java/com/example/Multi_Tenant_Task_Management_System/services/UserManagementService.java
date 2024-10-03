package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    private final TenantRepository tenantRepository;

    @Autowired
    public UserManagementService(UserRepository userRepo, TenantRepository tenantRepo) {
        this.tenantRepository = tenantRepo;
        this.userRepository = userRepo;
    }

    // Register new user and assign to a tenant
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public void registerUser(String username, String email, String password, String role, Integer tenantId) {
        Optional<Tenant> tenantOptional = tenantRepository.findById(tenantId);
        if (tenantOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid tenant ID");
        }
        Tenant tenant = tenantOptional.get();
        User user = new User(tenant, username, email, password, role);
        userRepository.save(user);
    }

    // Manage user roles
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public User updateUserRole(Integer userId, String role) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        user.setRole(role);
        return userRepository.save(user);
    }

    // Update user profile
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public User updateUserProfile(Integer userId, String username, String email, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    // Deactivate or delete user
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    // View all users within a tenant
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public List<User> getUsersByTenant(Integer tenantId) {
        Optional<Tenant> tenantOptional = tenantRepository.findById(tenantId);
        if (tenantOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid tenant ID");
        }
        return userRepository.findByTenant(tenantOptional.get());
    }

    // View all users globally (for Super Admin)
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public User getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new EntityNotFoundException("User with id " + id + " is not present");
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public void deleteUserById(Integer id) {
        try {
            getUserById(id);
            userRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with id " + id + " is not present");
        }
    }
}