package com.healthflow.healthflowbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// AppointmentRequest DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private Long patientId;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private String status;
}