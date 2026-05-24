package com.mediconnect.clinical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReferralOrderRequest {

    @NotBlank
    private String referredToSpecialty;

    private String referredToProvider;
    private String priority; // ROUTINE, URGENT
    private String reasonForReferral;
    private String notes;
}
