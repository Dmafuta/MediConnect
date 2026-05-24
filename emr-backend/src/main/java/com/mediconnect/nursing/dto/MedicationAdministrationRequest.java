package com.mediconnect.nursing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicationAdministrationRequest {

    // Optional — links administration to a written prescription
    private Long prescriptionId;

    @NotBlank
    private String medicationName;

    @NotBlank
    private String dose;

    private String route;

    @NotNull
    private LocalDateTime administeredAt;

    private String status; // ADMINISTERED, HELD, REFUSED, MISSED
    private String holdReason;
    private String notes;
}
