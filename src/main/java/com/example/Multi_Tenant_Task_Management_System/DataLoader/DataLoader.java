package com.example.Multi_Tenant_Task_Management_System.DataLoader;

import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
                Tenant tenant = new Tenant();
                tenant.setName("Home");
                tenant.setDescription("tester");

                // Save and re-attach the tenant
                tenantRepository.save(tenant);

                // Create a default user with all necessary attributes
                User defaultUser = new User();
                defaultUser.setUsername("user");
                defaultUser.setEmail("user@example.com");

                // Create an instance of BCryptPasswordEncoder
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

// Encode the raw password (e.g., "user")
                String encodedPassword = passwordEncoder.encode("user");

// Set the encoded password in the User object
                defaultUser.setPassword(encodedPassword);

                defaultUser.setTenant(tenant);  // Set tenant
                defaultUser.setRole("SuperAdmin");  // Set role
                defaultUser.setAccountNonExpired(true);
                defaultUser.setAccountNonLocked(true);
                defaultUser.setCredentialsNonExpired(true);
                defaultUser.setEnabled(true);

                // Save the user to the database
                userRepository.save(defaultUser);
            }
        };
    }
}