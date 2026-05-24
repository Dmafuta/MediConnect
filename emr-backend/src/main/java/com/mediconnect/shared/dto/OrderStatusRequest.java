package com.mediconnect.shared.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequest {

    @NotBlank
    private String status;

    private String notes;
}
