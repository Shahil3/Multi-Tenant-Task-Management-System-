package com.example.Multi_Tenant_Task_Management_System.controller;

import com.example.Multi_Tenant_Task_Management_System.Models.UserModel;
import com.example.Multi_Tenant_Task_Management_System.dto.ResponseWithTime;
import com.example.Multi_Tenant_Task_Management_System.dto.UserDto;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private final UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService service) {
        this.userManagementService = service;
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseWithTime<List<User>> getAllUsers() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        return userManagementService.getAllUsers(user);
    }
    // Show form to create a new user
    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userDto", new UserDto(null, null, null, null, null));
        return "createUser";  // Template to create a user
    }

    // Handle form submission to create a new user
    @PostMapping("/users")
    public String registerUser(@ModelAttribute UserDto userDto, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        ResponseWithTime<String> response = userManagementService.registerUser(userDto, user);
        model.addAttribute("message", response.getData());
        model.addAttribute("executionTime", response.getExecutionTime());
        return "userResult";  // Result page after creating a user
    }

//    @GetMapping("/users/{user_id}")
//    @ResponseBody
//    public ResponseWithTime<User> getUserById(@PathVariable Integer user_id) throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserModel user = (UserModel) authentication.getPrincipal();
//        return userManagementService.getUserById(user_id, user);
//    }

    @PostMapping("/inactivate/users/{user_id}")
    public ResponseWithTime<String> inactivateUser(@PathVariable Integer user_id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        return userManagementService.inactivateUser(user_id, user);
    }

    @GetMapping("/users/delete")
    public String showDeleteUserForm(Model model) {
        return "deleteUserForm";  // HTML form to input user ID
    }

    // Handle form submission to delete a specific user
    @PostMapping("/users/delete")
    public String deleteUserById(@RequestParam("userId") Integer userId, Model model, Authentication authentication) throws Exception {
        // Retrieve the current user's information
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // Call the service method to delete the user with authorization check
        ResponseWithTime<String> response = userManagementService.deleteUser(userId, currentUser);

        // Add response data to the model to display in the result page
        model.addAttribute("message", response.getData());
        model.addAttribute("executionTime", response.getExecutionTime());

        return "userResult";  // Result page after deleting user
    }

    @GetMapping("users/{userId}")
    public String getUserProfile(@PathVariable Integer userId, Model model, Authentication authentication) throws Exception {
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // Check if the current user has access to view the specified user's profile
        ResponseWithTime<User> response = userManagementService.getUserById(userId, currentUser);



        // Add user and execution time data to the model
        model.addAttribute("user", response.getData());
        model.addAttribute("executionTime", response.getExecutionTime());

        return "userProfile";
    }

    // Handle form submission for updating user profile
    @PostMapping("users/{userId}/update")
    public String updateUserProfile(
            @PathVariable Integer userId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            Authentication authentication) throws Exception {

        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // Check if the current user has access to update the specified user's profile
        ResponseWithTime<User> response = userManagementService.getUserById(userId, currentUser);



        // Update the user's profile
        ResponseWithTime<User> updateResponse = userManagementService.updateUserProfile(userId, username, email, password);
        model.addAttribute("message", "User profile updated successfully!");
        model.addAttribute("user", updateResponse.getData());
        model.addAttribute("executionTime", updateResponse.getExecutionTime());

        return "userResult";  // Result page after updating user profile
    }
}