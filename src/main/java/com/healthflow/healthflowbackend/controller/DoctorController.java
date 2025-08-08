package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.DTO.DoctorResponse;
import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        try {
            //  Fetch all users whose role is "doctor"
            List<User> doctors = userRepository.findByRole("doctor");
            System.out.println("Doctors: " + doctors);
            //  Map User entities to lightweight DTOs for the frontend
            List<DoctorResponse> response = doctors.stream()
                    .map(user -> new DoctorResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getSpecialty()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}