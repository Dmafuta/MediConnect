package com.mediconnect.pharmacy.controller;

import com.mediconnect.pharmacy.dto.MedicationPrescriptionRequest;
import com.mediconnect.pharmacy.dto.PrescriptionCreateResponse;
import com.mediconnect.pharmacy.entity.MedicationPrescription;
import com.mediconnect.pharmacy.service.MedicationPrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final MedicationPrescriptionService prescriptionService;

    public PrescriptionController(MedicationPrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('doctors-patientoverview-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Pharmacist')")
    public List<MedicationPrescription> getByPatient(@PathVariable Long patientId) {
        return prescriptionService.findByPatient(patientId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public PrescriptionCreateResponse create(@Valid @RequestBody MedicationPrescriptionRequest request) {
        return prescriptionService.create(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> discontinue(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
