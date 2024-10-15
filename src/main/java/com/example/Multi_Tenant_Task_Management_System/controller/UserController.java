package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.dto.UserDto;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAllUsers() {
        return userManagementService.getAllUsers();
    }

    @PostMapping("/users")
    public void registerUser(@RequestBody UserDto userDto) {
        userManagementService.registerUser(userDto);
    }

    @GetMapping("/users/{user_id}")
    public User getUserById(@PathVariable Integer user_id) {
        return userManagementService.getUserById(user_id);
    }

    @DeleteMapping("/users/{user_id}")
    public void deleteUserById(@PathVariable Integer user_id) {
        userManagementService.deleteUserById(user_id);
    }
}
