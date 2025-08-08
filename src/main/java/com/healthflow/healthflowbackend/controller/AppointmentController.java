package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.DTO.AppointmentRequest;
import com.healthflow.healthflowbackend.DTO.AppointmentResponse;
import com.healthflow.healthflowbackend.model.Appointment;
import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.AppointmentRepository;
import com.healthflow.healthflowbackend.repository.UserRepository;
import com.healthflow.healthflowbackend.services.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;


import java.util.Optional;

@RestController // Marks
@RequestMapping("/api/appointments")    // Base URL path for all endpoints in this controller
@CrossOrigin(origins =  "https://venerable-cannoli-933d82.netlify.app") // Allow requests from frontend deployment
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;  // Service layer for appointment-related logic

    @Autowired
    private UserRepository userRepository;  // Repository for interacting with User table

    // create
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            // Find doctor by ID from request
            User doctor = userRepository.findById(request.getDoctorId()).orElse(null);
            if (doctor == null) {
                doctor = new User();
                doctor.setId(request.getDoctorId());
                doctor.setFirstName("Dummy");
                doctor.setLastName("Doctor");
                doctor.setRole("DOCTOR");
                System.out.println("⚠️ Using dummy doctor (id: " + doctor.getId() + ")");
            }

            // Find patient by ID, throw error if not found
            User patient = userRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // Create and populate Appointment object
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            // String → java.sql.Date
            appointment.setAppointmentDate(java.sql.Date.valueOf(request.getAppointmentDate()));
            // String → java.sql.Time
            appointment.setStartTime(java.sql.Time.valueOf(request.getStartTime() + ":00"));
            appointment.setEndTime(java.sql.Time.valueOf(request.getEndTime() + ":00"));
            appointment.setStatus(request.getStatus());
            // Save appointment to database
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment booked successfully)");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to book appointment: " + e.getMessage());
        }
    }

    @Autowired
    private AppointmentRepository appointmentRepository;


    @GetMapping("/user/{id}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByUserId(@PathVariable Long id) {
        // Retrieve appointments for given patient ID
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(id);
        // Convert Appointment entities to AppointmentResponse DTOs
        List<AppointmentResponse> responses = appointments.stream()
                .map(appointment -> new AppointmentResponse(
                        appointment.getAppointmentId(),
                        appointment.getDoctor().getFullName(),
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDate().toLocalDate(),
                        appointment.getStartTime().toLocalTime(),
                        appointment.getEndTime().toLocalTime(),
                        appointment.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // cancel and delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        // Find appointment by ID
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Delete appointment
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Appointment> appointments;
        // If date is provided, filter by doctor ID and date
        if (date != null) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        } else {
            // Otherwise, get all appointments for the doctor
            appointments = appointmentRepository.findByDoctorId(doctorId);
        }
        // Convert entities to DTOs
        List<AppointmentResponse> responseList = appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(responseList);
    }

}