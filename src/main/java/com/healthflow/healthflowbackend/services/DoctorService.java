package com.healthflow.healthflowbackend.services;

import com.healthflow.healthflowbackend.DTO.DoctorResponse;
import com.healthflow.healthflowbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private UserRepository userRepository;

    public List<DoctorResponse> getAllDoctors() {
        return userRepository.findByRole("DOCTOR").stream()
                .map(user -> new DoctorResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getSpecialty()
                ))
                .collect(Collectors.toList());
    }
}
