package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.Models.UserModel;
import com.example.Multi_Tenant_Task_Management_System.dto.ResponseWithTime;
import com.example.Multi_Tenant_Task_Management_System.dto.UserDto;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserManagementService(UserRepository userRepo, TenantRepository tenantRepo) {
        this.tenantRepository = tenantRepo;
        this.userRepository = userRepo;
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public ResponseWithTime<String> registerUser(UserDto userDto, UserModel userModel) throws Exception {
        long startTime = System.currentTimeMillis();
        validateUserAccess(userDto, userModel);

        Tenant tenant = tenantRepository.findById(userDto.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant ID"));

        User user = new User(tenant, userDto.username(), userDto.email(), encoder.encode(userDto.password()), userDto.role());
        userRepository.save(user);

        return new ResponseWithTime<>("User registered successfully", System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @Cacheable(value = "users", key = "#tenantId")
    public ResponseWithTime<List<User>> getUsersByTenant(Integer tenantId) {
        long startTime = System.currentTimeMillis();
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant ID"));

        return new ResponseWithTime<>(userRepository.findByTenant(tenant), System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @Cacheable(value = "users", key = "#userId")
    public ResponseWithTime<User> getUserById(Integer userId, UserModel userModel) throws Exception {
        long startTime = System.currentTimeMillis();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not present"));

        validateActionAuthorization(user, userModel);

        return new ResponseWithTime<>(user, System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @Cacheable(value = "users")
    public ResponseWithTime<List<User>> getAllUsers(UserModel user) throws Exception {
        long startTime = System.currentTimeMillis();
        Integer tenantId = user.get_tenant_id();

        List<User> users = (tenantId == 1) ? userRepository.findAll() : userRepository.findByTenant(
                tenantRepository.findById(tenantId).orElseThrow(() -> new Exception("No such tenant present"))
        );

        return new ResponseWithTime<>(users, System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CachePut(value = "users", key = "#userId")
    @Transactional
    public ResponseWithTime<User> updateUserRole(Integer userId, String role) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setRole(role);
        userRepository.save(user);

        return new ResponseWithTime<>(user, System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CachePut(value = "users", key = "#userId")
    @Transactional
    public ResponseWithTime<User> updateUserProfile(Integer userId, String username, String email, String password) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);

        return new ResponseWithTime<>(user, System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CacheEvict(value = "users", allEntries = true)  // Evict all users from cache after deletion
    @Transactional
    public ResponseWithTime<String> deleteUser(Integer userId, UserModel userModel) throws Exception {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not present"));

        validateActionAuthorization(user, userModel);

        userRepository.deleteById(userId);
        return new ResponseWithTime<>("User deleted successfully", System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public ResponseWithTime<String> inactivateUser(Integer userId, UserModel userModel) throws Exception {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not present"));

        validateActionAuthorization(user, userModel);

        user.setAccountNonLocked(false);
        userRepository.save(user);

        return new ResponseWithTime<>("User inactivated successfully", System.currentTimeMillis() - startTime);
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public ResponseWithTime<String> deleteUserById(Integer userId, UserModel userModel) throws Exception {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not present"));

        validateActionAuthorization(user, userModel);

        userRepository.deleteById(userId);
        return new ResponseWithTime<>("User deleted successfully", System.currentTimeMillis() - startTime);
    }

    private void validateUserAccess(UserDto userDto, UserModel userModel) throws Exception {
        Integer tenantId = userModel.get_tenant_id();
        Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();
        boolean valid = authorities.stream().anyMatch(authority ->
                authority.getAuthority().equals("ROLE_SuperAdmin") ||
                        (authority.getAuthority().equals("ROLE_TenantAdmin") && Objects.equals(tenantId, userDto.tenantId()))
        );
        if (!valid) throw new Exception("Illegal action! You are not authorized to perform this action.");
    }

    private void validateActionAuthorization(User user, UserModel userModel) throws Exception {
        Integer tenantId = userModel.get_tenant_id();
        Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();
        boolean valid = authorities.stream().anyMatch(authority ->
                authority.getAuthority().equals("ROLE_SuperAdmin") ||
                        (authority.getAuthority().equals("ROLE_TenantAdmin") && Objects.equals(tenantId, user.getTenantId()))
        );

        if (!valid) throw new Exception("Illegal action! You are not authorized to perform this action.");
    }
}