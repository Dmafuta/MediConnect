package com.mediconnect.emergency.controller;

import com.mediconnect.emergency.dto.TriageAssessmentRequest;
import com.mediconnect.emergency.entity.TriageAssessment;
import com.mediconnect.emergency.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits/{visitId}/triage")
public class TriageController {

    private final TriageService triageService;

    public TriageController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('emergency-patient-triage-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<TriageAssessment> create(@PathVariable Long visitId,
                                                    @Valid @RequestBody TriageAssessmentRequest request) {
        return ResponseEntity.ok(triageService.create(visitId, request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('emergency-patient-triage-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<TriageAssessment> get(@PathVariable Long visitId) {
        return triageService.findByVisit(visitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('emergency-patient-triage-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<TriageAssessment> update(@PathVariable Long visitId,
                                                    @Valid @RequestBody TriageAssessmentRequest request) {
        return ResponseEntity.ok(triageService.update(visitId, request));
    }
}
