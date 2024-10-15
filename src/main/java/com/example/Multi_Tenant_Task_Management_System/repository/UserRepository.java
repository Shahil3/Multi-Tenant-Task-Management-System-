package com.example.Multi_Tenant_Task_Management_System.repository;


import com.example.Multi_Tenant_Task_Management_System.entity.Tenant;
import com.example.Multi_Tenant_Task_Management_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    public List<User> findByTenant(Tenant tenant);
    public User findByUsername(String userName);
}
