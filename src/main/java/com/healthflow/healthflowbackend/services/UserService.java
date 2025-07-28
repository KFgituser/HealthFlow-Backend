package com.healthflow.healthflowbackend.services;

import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
    }
    //register
    public User registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public User authenticate(String login, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(login);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(login);
        }

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }


        return null; // Invalid login
    }
    //原始 JDBC 调试方法
    @Autowired
    private DataSource dataSource;
    public void debugRawQueryByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ User found via JDBC:");
                System.out.println("user_id: " + rs.getLong("user_id"));
                System.out.println("email: " + rs.getString("email"));
                System.out.println("password: " + rs.getString("password"));
                // 可根据需要添加其他字段输出
            } else {
                System.out.println("❌ No user found with that email.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ JDBC query failed!");
        }
    }
}