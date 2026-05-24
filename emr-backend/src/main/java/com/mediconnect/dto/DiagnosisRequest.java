package com.mediconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiagnosisRequest {

    private String icd10Code;

    @NotBlank
    private String icd10Description;

    // PRIMARY, SECONDARY, DIFFERENTIAL
    private String diagnosisType;
}
