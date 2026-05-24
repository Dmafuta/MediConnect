package com.mediconnect.clinical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VisitStatusRequest {

    @NotBlank
    private String visitStatus; // QUEUED, IN_PROGRESS, COMPLETED, CANCELLED

    private String queueStatus;   // WAITING, WITH_PROVIDER, DONE
    private String billingStatus; // PENDING, BILLED, PAID
    private Boolean isTriaged;
}
