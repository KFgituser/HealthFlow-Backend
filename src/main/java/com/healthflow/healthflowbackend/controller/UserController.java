package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.services.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthflow.healthflowbackend.DTO.LoginRequest; //log in
import org.springframework.http.HttpStatus;
import com.healthflow.healthflowbackend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
// keep logged in
import jakarta.servlet.http.HttpSession;

@RestController     //Tells Spring: "This class is a REST controller."
@RequestMapping("/api/users")       //Sets a base URL for all the endpoints in this controller.
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")    // allow React frontend

public class UserController {
    //the logic for handling user-related requests.
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    //This maps HTTP GET requests to /api/users to the method getAllUsers().
    //It's used to retrieve all users.
    public String getAllUsers() {
        return userService.getAllUsers().toString();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getRole() == null || (!user.getRole().equalsIgnoreCase("doctor") && !user.getRole().equalsIgnoreCase("patient"))) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


        // for login
        @PostMapping("/auth/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

            String login = loginRequest.getLogin();
            String password = loginRequest.getPassword();

            Optional<User> userOpt = login.contains("@")
                    ? userRepository.findByEmail(login)
                    : userRepository.findByPhone(login);


            // Check user exists
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }

            User user = userOpt.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Check password
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                System.out.println("Password mismatch!");
                return ResponseEntity.status(401).body("Invalid password");
            }

            //  Store users in session so it's accessible later
            session.setAttribute("user", user);

            // Login success
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "role", user.getRole()
            ));


        }

        // keep logged in
        @PostMapping("/session-check")
        @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
        public ResponseEntity<?> checkSession(@RequestBody LoginRequest loginRequest, HttpSession session) {
            User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());

            if (user != null) {
                session.setAttribute("user", user); // ✅ Store user in session
                return ResponseEntity.ok(user);     // Return user info
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }
        @GetMapping("/session-check")
        public ResponseEntity<?> checkSession(HttpSession session) {
            Object user = session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
            }
            return ResponseEntity.ok(user);
        }

        // for logout
        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletRequest request) {
            request.getSession().invalidate(); // Invalidate the session
            return ResponseEntity.ok("Logged out successfully");
        }

    }

