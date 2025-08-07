package com.healthflow.healthflowbackend.DTO;



import com.healthflow.healthflowbackend.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long appointmentId;
    private String doctorName;
    private Long doctorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    public static AppointmentResponse fromEntity(Appointment appt) {
        AppointmentResponse dto = new AppointmentResponse();
        dto.appointmentId = appt.getAppointmentId();
        dto.doctorId = appt.getDoctor().getId();  //
        dto.doctorName = appt.getDoctor().getFullName(); //
        dto.appointmentDate = appt.getAppointmentDate().toLocalDate();
        dto.startTime = appt.getStartTime().toLocalTime();
        dto.endTime = appt.getEndTime().toLocalTime();
        dto.status = appt.getStatus();
        return dto;
    }

}
