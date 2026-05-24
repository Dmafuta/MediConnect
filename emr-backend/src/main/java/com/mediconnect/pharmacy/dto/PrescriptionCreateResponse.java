package com.mediconnect.pharmacy.dto;

import com.mediconnect.pharmacy.entity.MedicationPrescription;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrescriptionCreateResponse {
    private MedicationPrescription prescription;
    private List<String> allergyAlerts;

    public boolean hasAlerts() {
        return allergyAlerts != null && !allergyAlerts.isEmpty();
    }
}
