package com.mediconnect.patient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientProblemRequest {

    @NotBlank
    private String problemDescription;

    private String currentStatus;
    private String note;
    private LocalDate onsetDate;
    private LocalDate resolvedDate;
    private Boolean isResolved;
    private Boolean isPrincipalProblem;
}
