package com.healthflow.healthflowbackend.services;

import com.healthflow.healthflowbackend.model.Appointment;
import com.healthflow.healthflowbackend.model.User;
import com.healthflow.healthflowbackend.repository.AppointmentRepository;
import com.healthflow.healthflowbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    //construction injection
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    //for booking
    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }
    public List<Appointment> getAppointmentsByPatientId(Long userId) {
        return appointmentRepository.findByPatient_Id(userId);

    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }


    // add more methods as needed!
}