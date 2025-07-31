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
        dto.doctorId = appt.getDoctor().getId();  // 确保 Appointment 有 doctor 字段
        dto.doctorName = appt.getDoctor().getFullName(); // 确保 doctor 是 User 类型
        dto.appointmentDate = appt.getAppointmentDate().toLocalDate(); // 类型为 LocalDate
        dto.startTime = appt.getStartTime().toLocalTime();             // 类型为 LocalTime
        dto.endTime = appt.getEndTime().toLocalTime();                 // 类型为 LocalTime
        dto.status = appt.getStatus();
        return dto;
    }

}
