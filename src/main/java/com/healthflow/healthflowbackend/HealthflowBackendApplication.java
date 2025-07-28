package com.healthflow.healthflowbackend;

import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication

public class HealthflowBackendApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HealthflowBackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String dummyEmail = "hkf1239949@163.com";
        if (userRepository.findByEmail(dummyEmail).isEmpty()) {
            User dummyUser = new User();
            dummyUser.setUserName("Eric H");
            dummyUser.setEmail(dummyEmail);
            dummyUser.setPassword(passwordEncoder.encode("123456"));
            dummyUser.setRole("PATIENT");
            dummyUser.setFirstName("Eric");
            dummyUser.setLastName("H");

            userRepository.save(dummyUser);
            System.out.println("✅ Dummy user created!");
        } else {
            System.out.println("ℹ️ Dummy user already exists.");
        }
    }
}
