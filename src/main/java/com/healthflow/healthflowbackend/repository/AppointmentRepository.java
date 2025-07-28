package com.healthflow.healthflowbackend.repository;

import com.healthflow.healthflowbackend.model.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_Id(@Param("userId")Long patientId);
}