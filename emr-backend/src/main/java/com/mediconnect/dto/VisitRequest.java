package com.mediconnect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisitRequest {

    @NotNull
    private Long patientId;

    // Optional — null for walk-in visits
    private Long appointmentId;

    @NotNull
    private LocalDate visitDate;

    private LocalTime visitTime;

    @NotNull
    private Long providerId;

    private String departmentName;

    // OPD, IPD, EMERGENCY
    private String visitType;

    private Long parentVisitId;
}
