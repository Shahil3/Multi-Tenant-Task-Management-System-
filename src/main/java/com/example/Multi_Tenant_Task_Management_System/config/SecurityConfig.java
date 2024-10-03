package com.example.Multi_Tenant_Task_Management_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Allow access to the login page
                        .requestMatchers("/").permitAll() // Allow access to the root path
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")// Configure form login
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true") // Redirect to the login page with error parameter
                        .permitAll()
                );


        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("superAdmin").password("{noop}password").roles("SuperAdmin").build(),
                User.withUsername("tenantAdmin").password("{noop}password").roles("TenantAdmin").build(),
                User.withUsername("manager").password("{noop}password").roles("Manager").build(),
                User.withUsername("employee").password("{noop}password").roles("Employee").build()
        );
    }
}