package com.example.Multi_Tenant_Task_Management_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MultiTenantTaskManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiTenantTaskManagementSystemApplication.class, args);
	}

}
