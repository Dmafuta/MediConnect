package com.mediconnect.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabResultRequest {

    @NotBlank
    private String resultValue;

    private String resultUnit;
    private String resultRange;
    private String resultInterpretation; // NORMAL, ABNORMAL, CRITICAL
    private LocalDateTime resultDate;
}
