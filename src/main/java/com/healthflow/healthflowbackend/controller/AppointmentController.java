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

//delete
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "https://healthflow-backend-3l0y.onrender.com")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {

        try {
            // ✅ 兼容前端 dummy doctor 的写法
            User doctor = userRepository.findById(request.getDoctorId()).orElse(null);
            if (doctor == null) {
                doctor = new User();
                doctor.setId(request.getDoctorId());
                doctor.setFirstName("Dummy");
                doctor.setLastName("Doctor");
                doctor.setRole("DOCTOR");
                System.out.println("⚠️ Using dummy doctor (id: " + doctor.getId() + ")");
            }

            // ✅ patient 必须是数据库中的真实用户
            User patient = userRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            // String → java.sql.Date
            appointment.setAppointmentDate(java.sql.Date.valueOf(request.getAppointmentDate()));

            // String → java.sql.Time
            appointment.setStartTime(java.sql.Time.valueOf(request.getStartTime() + ":00"));
            appointment.setEndTime(java.sql.Time.valueOf(request.getEndTime() + ":00"));
            appointment.setStatus(request.getStatus());

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
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(id);

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

    // cancel delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        appointmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Appointment> appointments;
        if (date != null) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        } else {
            appointments = appointmentRepository.findByDoctorId(doctorId);
        }

        List<AppointmentResponse> responseList = appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(responseList);
    }

}