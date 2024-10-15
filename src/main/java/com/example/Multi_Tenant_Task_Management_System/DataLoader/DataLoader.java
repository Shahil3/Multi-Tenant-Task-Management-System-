package com.example.Multi_Tenant_Task_Management_System.DataLoader;

import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader {

    @Autowired
    private final UserRepository userRepository;  // Inject the UserRepository


    private final TenantRepository tenantRepository;  // Inject the TenantRepository

    @Autowired
    public DataLoader(UserRepository userRepository, TenantRepository tenantRepository){
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    @Bean
    CommandLineRunner loadDefaultUser() {
        return args -> {
            // Check if any user exists
            if (userRepository.count() == 0) {
                // Fetch or create the default Tenant (ensure the tenant exists)


                // Create a default user with all necessary attributes
                User defaultUser = new User();
                defaultUser.setUsername("user");
                defaultUser.setEmail("user@example.com");
                defaultUser.setPassword("user");  // Encode password
                defaultUser.setTenant(null);  // Set tenant
                defaultUser.setRole("SuperAdmin");  // Set role
                defaultUser.setAccountNonExpired(true);
                defaultUser.setAccountNonLocked(true);
                defaultUser.setCredentialsNonExpired(true);
                defaultUser.setEnabled(true);
                userRepository.save(defaultUser);  // Save the user to the database
            }
        };
    }
}