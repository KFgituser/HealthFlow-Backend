package com.healthflow.healthflowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication

public class HealthflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthflowBackendApplication.class, args);

     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();




        String hashed = encoder.encode("123456");
        System.out.println("✅ 正确哈希是：" + hashed);
    }

}
