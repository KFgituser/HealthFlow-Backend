package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.services.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import com.healthflow.healthflowbackend.DTO.LoginRequest; //log in
import org.springframework.http.HttpStatus;
import com.healthflow.healthflowbackend.repository.UserRepository;


import java.util.List;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
// keep logged in
import jakarta.servlet.http.HttpSession;

@RestController     //Tells Spring: "This class is a REST controller."
@RequestMapping("/api/users")       //Sets a base URL for all the endpoints in this controller.
@CrossOrigin(origins = "https://healthflow-frontend-sl57.onrender.com", allowCredentials = "true")    // allow React frontend

public class UserController {
    //the logic for handling user-related requests.
    @Autowired
    private UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository,  UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }


    @GetMapping
    //This maps HTTP GET requests to /api/users to the method getAllUsers().
    //It's used to retrieve all users.
    public String getAllUsers() {
        return userService.getAllUsers().toString();
    }

        //register
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
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
            System.out.println("✅ Login endpoint hit!");
            String email = loginRequest.getEmail();
            String rawPassword = loginRequest.getPassword();

            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

            // Check user exists
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 用户未找到！");
            }
            User user = userOptional.get();
            // Check user password
            if (user.getPassword() == null) {
                System.out.println("⚠️ 数据库中密码为 null！");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户密码为空！");
            }
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
            System.out.println("用户信息：" + user);
            System.out.println("用户邮箱：" + loginRequest.getEmail());
            //  Store users in session so it's accessible later
            session.setAttribute("user", user);

            // Login success
            return ResponseEntity.ok(user);

        }


        // keep logged in
        @PostMapping("/session-check")
        @CrossOrigin(origins = "https://healthflow-backend-3l0y.onrender.com", allowCredentials = "true")
        public ResponseEntity<?> checkSession(@RequestBody LoginRequest loginRequest, HttpSession session) {
            User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            if (user != null) {
                session.setAttribute("user", user); // ✅ Store user in session
                return ResponseEntity.ok(user);     // Return user info
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }

    @GetMapping("/session-check")
    public ResponseEntity<?> checkSession(HttpSession session, HttpServletRequest request) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        User loggedInUser = (User) user;
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loggedInUser,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + loggedInUser.getRole().toUpperCase()))
                );
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return ResponseEntity.ok(loggedInUser);
    }


    // for logout
        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletRequest request) {
            request.getSession().invalidate(); // Invalidate the session
            return ResponseEntity.ok("Logged out successfully");
        }

    }

