package com.mediconnect.lab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabOrderRequest {

    @NotBlank
    private String testName;

    private String testCode;
    private String priority; // ROUTINE, URGENT, STAT
    private String clinicalIndication;
    private String notes;
}
