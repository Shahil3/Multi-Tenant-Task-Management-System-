package com.example.Multi_Tenant_Task_Management_System.Models;

import com.example.Multi_Tenant_Task_Management_System.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.text.Style;
import java.util.*;
import java.util.List;

public class UserModel implements UserDetails {
    private final User user;
    public UserModel(User user){
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }

    public Integer get_tenant_id(){
        return user.getTenantId();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }
    public void setAccountNonLocked(boolean status){
        user.setAccountNonLocked(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
