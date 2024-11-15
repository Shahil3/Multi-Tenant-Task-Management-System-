package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.Models.UserModel;
import com.example.Multi_Tenant_Task_Management_System.dto.ProjectDto;
import com.example.Multi_Tenant_Task_Management_System.entity.Project;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.repository.ProjectRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private  final TenantRepository tenantRepository;
    private  final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,TenantRepository tenantRepository,UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    public void createProject(ProjectDto projectDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();

        Tenant tenant = tenantRepository.findById(user.get_tenant_id())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found for user"));

        System.out.println("status is " + projectDto.status()+"******************");
        Project project = new Project(tenant, projectDto.name(), projectDto.description(), projectDto.status());

        projectRepository.save(project);
    }

    public void assignProjectToEmployee(Integer projectId, Integer userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project ID"));

        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Employee User ID"));

        if (!employee.getRole().equals("Employee") && !employee.getRole().equals("Manager")) {
            throw new IllegalArgumentException("The selected user is not eligible to be assigned to a project.");
        }

        project.assignEmployee(employee);
        projectRepository.save(project); // Save the updated project with the new assignment
    }


}