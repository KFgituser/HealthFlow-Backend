package com.healthflow.healthflowbackend.repository;

import com.healthflow.healthflowbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);   //find email
     Optional<User> findByPhone(String phone); //find phone
     List<User> findByRole(String role);
}