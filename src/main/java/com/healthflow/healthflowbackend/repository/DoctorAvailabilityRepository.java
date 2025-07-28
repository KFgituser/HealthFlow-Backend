package com.healthflow.healthflowbackend.repository;

import com.healthflow.healthflowbackend.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

}