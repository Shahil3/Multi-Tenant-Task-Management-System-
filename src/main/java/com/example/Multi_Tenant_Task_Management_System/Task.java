package com.example.Multi_Tenant_Task_Management_System;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;  // Task belongs to a project

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_to", nullable = true)
    private User assignedTo;  // Optional field representing the user to whom the task is assigned

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private State_status status;  // e.g., Pending, In Progress, Completed
    private enum State_status{
        In_Progress,Completed
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 10)
    private State_priority priority;  // e.g., Low, Medium, High
    private enum State_priority{
        Low, Medium, High
    }

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;  // One task can have many comments

    // Constructors
    public Task() {}

    public Task(Project project, String name, String description, User assignedTo, String status, String priority, LocalDateTime deadline) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        setStatus(status);
        setPriority(priority);
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(String status) {
        try{
            this.status = State_status.valueOf(status);
        }catch (IllegalArgumentException exp){
            System.out.println("Incorrect status");
        }
    }

    public String getPriority() {
        return priority.name();
    }

    public void setPriority(String priority) {
        try{
            this.priority = State_priority.valueOf(priority);
        }catch (IllegalArgumentException exp){
            System.out.println("Incorrect Value");
        }
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}