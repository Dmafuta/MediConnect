package com.mediconnect.radiology.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImagingOrderRequest {

    @NotBlank
    private String studyType; // X_RAY, CT, MRI, ULTRASOUND, etc.

    private String bodyPart;
    private String priority; // ROUTINE, URGENT, STAT
    private String clinicalIndication;
    private String notes;
}
