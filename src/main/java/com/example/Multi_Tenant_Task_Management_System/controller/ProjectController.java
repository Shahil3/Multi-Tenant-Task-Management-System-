package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.dto.ProjectDto;
import com.example.Multi_Tenant_Task_Management_System.entity.Project;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Display form to create a new project
    @GetMapping("/new")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("projectDto", new ProjectDto(null, null, "ACTIVE")); // Default status as ACTIVE
        return "createProject";  // HTML form for creating a project
    }

    // Handle form submission to create a new project
    @PostMapping
    public String createProject(@ModelAttribute ProjectDto projectDto, Model model) {
        projectService.createProject(projectDto);
        model.addAttribute("message", "Project created successfully!");
        return "projectResult";  // Result page after creating a project
    }


    // Show form to assign project
    @GetMapping("/assign/new")
    public String showAssignProjectForm() {
        return "assignProject";
    }

    // Handle form submission for project assignment
    @PostMapping("/assign")
    public String assignProjectToEmployee(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("userId") Integer userId,
            Model model) {

        try {
            projectService.assignProjectToEmployee(projectId, userId);
            model.addAttribute("message", "Project assigned successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "assignmentResult";
    }

}