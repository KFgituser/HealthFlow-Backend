package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.model.DoctorAvailability;
import com.healthflow.healthflowbackend.services.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-availability")
@CrossOrigin(origins =  "https://venerable-cannoli-933d82.netlify.app/")    // allow React frontend
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;

    public DoctorAvailabilityController(DoctorAvailabilityService service) {
        this.service = service;
    }

    @GetMapping
    public List<DoctorAvailability> getAll() {
        return service.findAll();
    }

    @PostMapping
    public DoctorAvailability create(@RequestBody DoctorAvailability availability) {
        return service.save(availability);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}