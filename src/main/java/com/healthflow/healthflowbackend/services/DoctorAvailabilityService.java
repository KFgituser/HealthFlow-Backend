package com.healthflow.healthflowbackend.services;

import com.healthflow.healthflowbackend.model.DoctorAvailability;
import com.healthflow.healthflowbackend.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository repository) {
        this.repository = repository;
    }

    public List<DoctorAvailability> findAll() {
        return repository.findAll();
    }

    public DoctorAvailability save(DoctorAvailability availability) {
        return repository.save(availability);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
