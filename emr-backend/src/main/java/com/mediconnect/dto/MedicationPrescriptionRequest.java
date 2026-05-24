package com.mediconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicationPrescriptionRequest {

    @NotNull
    private Long patientId;

    // Optional visit context
    private Long visitId;

    @NotBlank
    private String medicationName;

    private String route;

    @NotBlank
    private String dose;

    @NotBlank
    private String frequency;

    private Integer duration;
    private String durationType;
    private Integer refill;
    private String typeOfMedication;
    private String instructions;
}
