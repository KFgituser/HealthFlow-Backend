package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.model.DoctorAvailability;
import com.healthflow.healthflowbackend.services.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-availability")
@CrossOrigin(origins =  "https://venerable-cannoli-933d82.netlify.app")    // allow React frontend
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;
    // Constructor injection for the service
    public DoctorAvailabilityController(DoctorAvailabilityService service) {
        this.service = service;
    }

    // GET: Retrieve all doctor availability records
    @GetMapping
    public List<DoctorAvailability> getAll() {
        return service.findAll();
    }

    // POST: Create a new doctor availability entry
    @PostMapping
    public DoctorAvailability create(@RequestBody DoctorAvailability availability) {
        return service.save(availability);
    }

    // DELETE: Remove a doctor availability record by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}