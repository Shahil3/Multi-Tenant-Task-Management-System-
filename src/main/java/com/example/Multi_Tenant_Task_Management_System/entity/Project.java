package com.example.Multi_Tenant_Task_Management_System.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;


@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer project_id;

    @Version
    private Integer version;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "project_name",nullable = false)
    private String name;

    @Column(name = "project_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status",nullable = false)
    private State status;
    private enum State{
        Active, Inactive, Completed
    }

    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignedEmployees = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    Constructor
    public Project(){}

    public Project(Tenant tenant, String name, String description, String status) {
        this.tenant = tenant;
        this.name = name;
        this.description = description;
        setStatus(status); // Use the setter to validate and set the status
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    // Method to add an employee to the project
    public void assignEmployee(User employee) {
        if (!assignedEmployees.contains(employee)) {
            assignedEmployees.add(employee);
        }
    }


//    Getters and Setters
    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(String status) {
        if (status != null) {
            try {
                this.status = State.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid project status: " + status);
            }
        } else {
            throw new IllegalArgumentException("Status cannot be null");
        }
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


}

