package com.mediconnect.controller;

import com.mediconnect.dto.*;
import com.mediconnect.entity.*;
import com.mediconnect.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;
    private final VitalsService vitalsService;
    private final DiagnosisService diagnosisService;
    private final MedicationPrescriptionService prescriptionService;
    private final ClinicalNoteService noteService;

    public VisitController(VisitService visitService, VitalsService vitalsService,
                            DiagnosisService diagnosisService,
                            MedicationPrescriptionService prescriptionService,
                            ClinicalNoteService noteService) {
        this.visitService = visitService;
        this.vitalsService = vitalsService;
        this.diagnosisService = diagnosisService;
        this.prescriptionService = prescriptionService;
        this.noteService = noteService;
    }

    // ── Visit CRUD ──────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAuthority('appointment-listvisit-view') or hasRole('System Admin')")
    public Page<Visit> getAllVisits(@PageableDefault(size = 15) Pageable pageable) {
        return visitService.findAll(pageable);
    }

    @GetMapping("/queue/today")
    @PreAuthorize("hasAuthority('appointment-listvisit-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<Visit> getTodaysQueue() {
        return visitService.findTodaysQueue();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('appointment-listvisit-view') or hasRole('System Admin')")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        return visitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('appointment-visit-view') or hasRole('System Admin') or hasRole('Receptionist')")
    public Visit createVisit(@Valid @RequestBody VisitRequest request) {
        return visitService.create(request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('appointment-visit-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<Visit> updateStatus(@PathVariable Long id,
                                               @Valid @RequestBody VisitStatusRequest request) {
        return ResponseEntity.ok(visitService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('System Admin')")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Vitals ──────────────────────────────────────────────────

    @GetMapping("/{id}/vitals")
    @PreAuthorize("hasAuthority('doctors-patientoverview-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<Vitals> getVitals(@PathVariable Long id) {
        return vitalsService.findByVisit(id);
    }

    @PostMapping("/{id}/vitals")
    @PreAuthorize("hasAuthority('Clinical-notes-outpatExamination-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public Vitals addVitals(@PathVariable Long id, @RequestBody VitalsRequest request) {
        return vitalsService.create(id, request);
    }

    @PutMapping("/{visitId}/vitals/{vitalsId}")
    @PreAuthorize("hasAuthority('Clinical-notes-outpatExamination-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<Vitals> updateVitals(@PathVariable Long visitId,
                                                @PathVariable Long vitalsId,
                                                @RequestBody VitalsRequest request) {
        return ResponseEntity.ok(vitalsService.update(vitalsId, request));
    }

    @DeleteMapping("/{visitId}/vitals/{vitalsId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> deleteVitals(@PathVariable Long visitId, @PathVariable Long vitalsId) {
        vitalsService.delete(vitalsId);
        return ResponseEntity.noContent().build();
    }

    // ── Diagnoses ───────────────────────────────────────────────

    @GetMapping("/{id}/diagnoses")
    @PreAuthorize("hasAuthority('doctors-patientoverview-view') or hasRole('System Admin') or hasRole('Doctor')")
    public List<Diagnosis> getDiagnoses(@PathVariable Long id) {
        return diagnosisService.findByVisit(id);
    }

    @PostMapping("/{id}/diagnoses")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public Diagnosis addDiagnosis(@PathVariable Long id, @Valid @RequestBody DiagnosisRequest request) {
        return diagnosisService.create(id, request);
    }

    @PutMapping("/{visitId}/diagnoses/{diagnosisId}")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Diagnosis> updateDiagnosis(@PathVariable Long visitId,
                                                      @PathVariable Long diagnosisId,
                                                      @Valid @RequestBody DiagnosisRequest request) {
        return ResponseEntity.ok(diagnosisService.update(diagnosisId, request));
    }

    @DeleteMapping("/{visitId}/diagnoses/{diagnosisId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long visitId, @PathVariable Long diagnosisId) {
        diagnosisService.delete(diagnosisId);
        return ResponseEntity.noContent().build();
    }

    // ── Prescriptions (within visit context) ───────────────────

    @GetMapping("/{id}/prescriptions")
    @PreAuthorize("hasAuthority('doctors-patientoverview-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Pharmacist')")
    public List<MedicationPrescription> getPrescriptions(@PathVariable Long id) {
        return prescriptionService.findByVisit(id);
    }

    @PostMapping("/{id}/prescriptions")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public MedicationPrescription addPrescription(@PathVariable Long id,
                                                   @Valid @RequestBody MedicationPrescriptionRequest request) {
        request.setVisitId(id);
        return prescriptionService.create(request);
    }

    // ── Clinical Notes (SOAP) ───────────────────────────────────

    @GetMapping("/{id}/notes")
    @PreAuthorize("hasAuthority('clinical-patient-visit-note-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<ClinicalNote> getNotes(@PathVariable Long id) {
        return noteService.findByVisit(id);
    }

    @PostMapping("/{id}/notes")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ClinicalNote addNote(@PathVariable Long id, @RequestBody ClinicalNoteRequest request) {
        return noteService.create(id, request);
    }

    @PutMapping("/{visitId}/notes/{noteId}")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<ClinicalNote> updateNote(@PathVariable Long visitId,
                                                    @PathVariable Long noteId,
                                                    @RequestBody ClinicalNoteRequest request) {
        return ResponseEntity.ok(noteService.update(noteId, request));
    }

    @PutMapping("/{visitId}/notes/{noteId}/finalize")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<ClinicalNote> finalizeNote(@PathVariable Long visitId, @PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.finalize(noteId));
    }

    @DeleteMapping("/{visitId}/notes/{noteId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long visitId, @PathVariable Long noteId) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }
}
