package com.healthflow.healthflowbackend.controller;

import com.healthflow.healthflowbackend.DTO.AppointmentRequest;
import com.healthflow.healthflowbackend.DTO.AppointmentResponse;
import com.healthflow.healthflowbackend.model.Appointment;
import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.AppointmentRepository;
import com.healthflow.healthflowbackend.repository.UserRepository;
import com.healthflow.healthflowbackend.services.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.util.stream.Collectors;

//delete
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔧 Logged in user: " + auth.getName());

        // 查找医生和病人
        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 日期/时间处理
        LocalDate localDate = LocalDate.parse(dto.getAppointmentDate());
        LocalTime startLocal = LocalTime.parse(dto.getStartTime());
        LocalTime endLocal = LocalTime.parse(dto.getEndTime());

        //  SQL
        Date appointmentDate = Date.valueOf(localDate);
        Time startTime = Time.valueOf(startLocal);
        Time endTime = Time.valueOf(endLocal);

        // 构建预约对象
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(dto.getStatus());

        Appointment saved = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(saved);
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
}