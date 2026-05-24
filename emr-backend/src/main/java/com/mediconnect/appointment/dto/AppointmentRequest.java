package com.mediconnect.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    // Null for walk-in patients
    private Long patientId;

    // Walk-in fields (required when patientId is null)
    private String walkinFirstName;
    private String walkinLastName;
    private String walkinContactNumber;
    private String walkinGender;

    @NotNull
    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    @NotNull
    private Long providerId;

    private String departmentName;
    private String appointmentType;
    private String reason;
}
