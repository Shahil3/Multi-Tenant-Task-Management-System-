package com.example.Multi_Tenant_Task_Management_System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
    @GetMapping("/login")
    public String login() {
        return "login"; // This should match the name of your login HTML file (e.g., login.html)
    }
}
