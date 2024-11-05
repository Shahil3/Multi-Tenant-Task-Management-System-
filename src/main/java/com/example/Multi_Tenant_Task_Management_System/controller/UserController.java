package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.Models.UserModel;
import com.example.Multi_Tenant_Task_Management_System.dto.UserDto;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService service) {
        this.userManagementService = service;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        return userManagementService.getAllUsers(user);
    }

    @PostMapping("/users")
    public void registerUser(@RequestBody UserDto userDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        userManagementService.registerUser(userDto,user);
    }

    @GetMapping("/users/{user_id}")
    public User getUserById(@PathVariable Integer user_id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        return userManagementService.getUserById(user_id,user);
    }
    @PostMapping("/inactivate/users/{user_id}")
    public String inactivateUser(@PathVariable Integer user_id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        userManagementService.inactivateUser(user_id,user);
        return "done";
    }

    @DeleteMapping("/users/{user_id}")
    public void deleteUserById(@PathVariable Integer user_id) {
        userManagementService.deleteUserById(user_id);
    }
}
