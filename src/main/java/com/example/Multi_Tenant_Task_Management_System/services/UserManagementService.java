package com.example.Multi_Tenant_Task_Management_System.services;

import com.example.Multi_Tenant_Task_Management_System.Models.UserModel;
import com.example.Multi_Tenant_Task_Management_System.dto.UserDto;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.repository.UserRepository;
import com.example.Multi_Tenant_Task_Management_System.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    // Register new user and assign to a tenant
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    public void registerUser(UserDto userDto, UserModel userModel) throws Exception {
        Integer tenant_id = userModel.get_tenant_id();
        Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();
        boolean valid = false;

        for(GrantedAuthority authority : authorities){
            System.out.println(authority.getAuthority());
            System.out.println(userDto.tenantId()+ "+ "+ tenant_id);
            if(authority.getAuthority().equals("ROLE_SuperAdmin") || (authority.getAuthority().equals("ROLE_TenantAdmin") && Objects.equals(tenant_id, userDto.tenantId()))){
                valid = true;
            }
        }
        if(!valid){
            throw new Exception("illegal Action! you are not verified to perform this action!!!");
        }

        if (userDto.tenantId() == null) {
            throw new IllegalArgumentException("Tenant ID must not be null");
        }
        Optional<Tenant> tenantOptional = tenantRepository.findById(userDto.tenantId());
        if (tenantOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid tenant ID");
        }
        Tenant tenant = tenantOptional.get();
        User user = new User(tenant, userDto.username(), userDto.email(), encoder.encode(userDto.password()), userDto.role());
        userRepository.save(user);
    }

    // Manage user roles
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public User updateUserRole(Integer userId, String role) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        user.setRole(role);
        return userRepository.save(user);
    }

    // Update user profile
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public User updateUserProfile(Integer userId, String username, String email, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }

    // Deactivate or delete user
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    // View all users within a tenant
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public List<User> getUsersByTenant(Integer tenantId) {
        Optional<Tenant> tenantOptional = tenantRepository.findById(tenantId);
        if (tenantOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid tenant ID");
        }
        return userRepository.findByTenant(tenantOptional.get());
    }

    // View all users globally (for Super Admin)
    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    public List<User> getAllUsers(UserModel user) throws Exception {
        Integer tenantId = user.get_tenant_id();
        if(tenantId == 1)return userRepository.findAll();
        if(tenantRepository.findById(tenantId).isEmpty())throw new Exception("no such tenant present");
        else return userRepository.findByTenant(tenantRepository.findById(tenantId).get());

    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin')")
    public User getUserById(Integer id,UserModel userModel) throws Exception {

        Integer tenant_id = userModel.get_tenant_id();
        Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();
        boolean valid = false;

        for(GrantedAuthority authority : authorities){
            System.out.println(authority.getAuthority());
            if(authority.getAuthority().equals("ROLE_SuperAdmin") || (authority.getAuthority().equals("ROLE_TenantAdmin") && Objects.equals(tenant_id, id))){
                valid = true;
            }
        }
        if(!valid){
            throw new Exception("illegal Action! you are not verified to perform this action!!!");
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else throw new EntityNotFoundException("User with id " + id + " is not present");
    }

    @PreAuthorize("hasAnyRole('SuperAdmin', 'TenantAdmin') and @userManagementService.isUserAuthorized(#tenantId)")
    public void deleteUserById(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel userModel = (UserModel) authentication.getPrincipal();
        try {
            getUserById(id,userModel);
            userRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with id " + id + " is not present");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void inactivateUser(Integer userId, UserModel userModel) throws Exception {
        Integer tenant_id = userModel.get_tenant_id();
        Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();
        boolean valid = false;

        for(GrantedAuthority authority : authorities){
            System.out.println(authority.getAuthority());
            if(authority.getAuthority().equals("ROLE_SuperAdmin") || (authority.getAuthority().equals("ROLE_TenantAdmin") && Objects.equals(tenant_id, userId))){
                valid = true;
            }
        }
        if(!valid){
            throw new Exception("illegal Action! you are not verified to perform this action!!!");
        }
        userModel.setAccountNonLocked(true);
    }
}