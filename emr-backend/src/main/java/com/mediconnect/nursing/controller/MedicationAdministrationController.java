package com.mediconnect.nursing.controller;

import com.mediconnect.nursing.dto.MedicationAdministrationRequest;
import com.mediconnect.nursing.entity.MedicationAdministration;
import com.mediconnect.nursing.service.MedicationAdministrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicationAdministrationController {

    private final MedicationAdministrationService marService;

    public MedicationAdministrationController(MedicationAdministrationService marService) {
        this.marService = marService;
    }

    @GetMapping("/api/visits/{visitId}/administrations")
    @PreAuthorize("hasAuthority('clinical-patient-visit-note-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse') or hasRole('Pharmacist')")
    public List<MedicationAdministration> getByVisit(@PathVariable Long visitId) {
        return marService.findByVisit(visitId);
    }

    @PostMapping("/api/visits/{visitId}/administrations")
    @PreAuthorize("hasAuthority('Clinical-notes-outpatExamination-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse') or hasRole('Pharmacist')")
    public MedicationAdministration record(@PathVariable Long visitId,
                                           @Valid @RequestBody MedicationAdministrationRequest request) {
        return marService.record(visitId, request);
    }

    @GetMapping("/api/patients/{patientId}/administrations")
    @PreAuthorize("hasAuthority('clinical-patient-visit-note-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse') or hasRole('Pharmacist')")
    public List<MedicationAdministration> getByPatient(@PathVariable Long patientId) {
        return marService.findByPatient(patientId);
    }

    @DeleteMapping("/api/administrations/{id}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        marService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
