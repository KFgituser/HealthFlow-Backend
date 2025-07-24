package com.healthflow.healthflowbackend.repository;

import com.healthflow.healthflowbackend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Custom query methods
}