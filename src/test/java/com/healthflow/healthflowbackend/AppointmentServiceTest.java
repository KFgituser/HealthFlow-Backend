package com.healthflow.healthflowbackend;

import com.healthflow.healthflowbackend.model.Appointment;
import com.healthflow.healthflowbackend.repository.AppointmentRepository;
import com.healthflow.healthflowbackend.services.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void testCreateAppointment() {
        Appointment appointment = new Appointment();
        Mockito.when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(appointment);

        assertNotNull(result);
    }
}