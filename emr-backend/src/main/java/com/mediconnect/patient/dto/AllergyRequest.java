package com.mediconnect.patient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AllergyRequest {

    @NotBlank
    private String allergenName;

    private String allergyType;
    private String severity;
    private Boolean verified;
    private String reaction;
    private String comments;
}
