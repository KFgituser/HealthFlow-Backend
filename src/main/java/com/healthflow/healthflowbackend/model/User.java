package com.healthflow.healthflowbackend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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
    private String password;

    private String dob;
    private String sex;
    @Column(name = "specialty", nullable = true)
    private String specialty;
    private String homeAddress;
    private String workLocation;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
