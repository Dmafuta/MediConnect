package com.mediconnect.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentStatusRequest {

    @NotBlank
    private String status; // ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW

    private String cancelledRemarks;
    private LocalDateTime cancelledOn;
    private String cancelledBy;
}
