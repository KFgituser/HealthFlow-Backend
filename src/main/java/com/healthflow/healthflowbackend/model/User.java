package com.healthflow.healthflowbackend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role")
    private String role;

    @Column(name = "user_name")
    private String userName;
    private String firstName;
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password_hash")
    private String passwordHash; //  PATIENT, DOCTOR, ADMIN
    private String password;

    private String dob;
    private String sex;
    private String specialty;
    private String homeAddress;
    private String workLocation;
}
