package com.example.Multi_Tenant_Task_Management_System.config;

import com.example.Multi_Tenant_Task_Management_System.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public SecurityConfig(MyUserDetailsService service) {
        this.userDetailsService = service;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        // Allow access to static resources without authentication
                        .requestMatchers("/","/styles.css", "/static/**", "/webjars/**").permitAll()
                        // Restrict access to tenant-specific URLs
                        .requestMatchers("/tenants/{tenant_id:[0-9]+}/**").hasAnyRole("SuperAdmin", "TenantAdmin")
                        .requestMatchers("/tenants/**").hasRole("SuperAdmin")  // Only SuperAdmin can access /tenants
                        .anyRequest().authenticated()  // All other requests must be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page
                        .defaultSuccessUrl("/", true)  // Redirect to "/" on successful login
                        .permitAll())  // Allow everyone to access the login page
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout").permitAll())  // Redirect after logout
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));  // Enable session creation

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}