package com.example.Multi_Tenant_Task_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")  // Maps the class to the 'users' table.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // Primary Key column in the database.
    private Integer userId;  // Use Integer for user_id as it's auto-generated.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id",nullable = false)  // Foreign key to the tenants table.
    @JsonIgnore
    private Tenant tenant;  // Link the Users entity to the Tenant entity.

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;  // Role field to differentiate between user roles (e.g., 'Admin', 'Manager', etc.)

    private enum Role {
        SuperAdmin, TenantAdmin, Manager, Employee
    }

    // Fields with default values set to true.
    @Column(name = "isAccountNonExpired", nullable = false)
    private Boolean isAccountNonExpired = true;

    @Column(name = "isAccountNonLocked", nullable = false)
    private Boolean isAccountNonLocked = true;

    @Column(name = "isCredentialsNonExpired", nullable = false)
    private Boolean isCredentialsNonExpired = true;

    @Column(name = "isEnabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(Tenant tenant, String username, String email, String password, String role) {
        this.tenant = tenant;
        this.username = username;
        this.email = email;
        this.password = password;
        setRole(role);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Integer getTenantId() {
        return tenant != null ? tenant.getTenantId() : null;
    }

    public String getTenantName() {
        return tenant != null ? tenant.getName() : null;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role.name();
    }

    public void setRole(String role) {
        try {
            this.role = Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified. Allowed values are: SuperAdmin, TenantAdmin, Manager, Employee.");
        }
    }

    public Boolean getAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility method to check if user is active
    public boolean isActive() {
        return isAccountNonExpired && isAccountNonLocked && isCredentialsNonExpired && isEnabled;
    }

    // Override toString() for better readability
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", tenant=" + tenant.getName() +  // Display tenant name instead of full object
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
