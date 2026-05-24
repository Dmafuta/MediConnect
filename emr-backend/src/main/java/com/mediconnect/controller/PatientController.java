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
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final AllergyService allergyService;
    private final PatientProblemService problemService;
    private final MedicationPrescriptionService prescriptionService;
    private final VisitService visitService;

    public PatientController(PatientService patientService, AllergyService allergyService,
                              PatientProblemService problemService,
                              MedicationPrescriptionService prescriptionService,
                              VisitService visitService) {
        this.patientService = patientService;
        this.allergyService = allergyService;
        this.problemService = problemService;
        this.prescriptionService = prescriptionService;
        this.visitService = visitService;
    }

    // ── Patient CRUD ────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin')")
    public Page<Patient> getAllPatients(@PageableDefault(size = 15) Pageable pageable) {
        return patientService.findAllPatients(pageable);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('patient-searchpatient-view') or hasRole('System Admin')")
    public Page<Patient> searchPatients(@RequestParam String q,
                                         @PageableDefault(size = 15) Pageable pageable) {
        return patientService.searchPatients(q, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin')")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.findPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('patient-register-view') or hasRole('System Admin')")
    public Patient createPatient(@Valid @RequestBody PatientCreateRequest request) {
        return patientService.createPatient(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('patient-register-view') or hasRole('System Admin')")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                  @Valid @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('System Admin')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    // ── Allergies ───────────────────────────────────────────────

    @GetMapping("/{id}/allergies")
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<Allergy> getAllergies(@PathVariable Long id) {
        return allergyService.findByPatient(id);
    }

    @PostMapping("/{id}/allergies")
    @PreAuthorize("hasAuthority('patient-register-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public Allergy addAllergy(@PathVariable Long id, @Valid @RequestBody AllergyRequest request) {
        return allergyService.create(id, request);
    }

    @PutMapping("/{patientId}/allergies/{allergyId}")
    @PreAuthorize("hasAuthority('patient-register-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Allergy> updateAllergy(@PathVariable Long patientId,
                                                  @PathVariable Long allergyId,
                                                  @Valid @RequestBody AllergyRequest request) {
        return ResponseEntity.ok(allergyService.update(allergyId, request));
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    @PreAuthorize("hasAuthority('patient-register-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long patientId, @PathVariable Long allergyId) {
        allergyService.delete(allergyId);
        return ResponseEntity.noContent().build();
    }

    // ── Problem List ────────────────────────────────────────────

    @GetMapping("/{id}/problems")
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<PatientProblem> getProblems(@PathVariable Long id,
                                             @RequestParam(defaultValue = "false") boolean activeOnly) {
        return activeOnly ? problemService.findActiveByPatient(id) : problemService.findByPatient(id);
    }

    @PostMapping("/{id}/problems")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public PatientProblem addProblem(@PathVariable Long id, @Valid @RequestBody PatientProblemRequest request) {
        return problemService.create(id, request);
    }

    @PutMapping("/{patientId}/problems/{problemId}")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<PatientProblem> updateProblem(@PathVariable Long patientId,
                                                         @PathVariable Long problemId,
                                                         @Valid @RequestBody PatientProblemRequest request) {
        return ResponseEntity.ok(problemService.update(problemId, request));
    }

    @DeleteMapping("/{patientId}/problems/{problemId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long patientId, @PathVariable Long problemId) {
        problemService.delete(problemId);
        return ResponseEntity.noContent().build();
    }

    // ── Prescriptions ───────────────────────────────────────────

    @GetMapping("/{id}/prescriptions")
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Pharmacist')")
    public List<MedicationPrescription> getPrescriptions(@PathVariable Long id) {
        return prescriptionService.findByPatient(id);
    }

    // ── Visit History ───────────────────────────────────────────

    @GetMapping("/{id}/visits")
    @PreAuthorize("hasAuthority('patient-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<Visit> getVisits(@PathVariable Long id) {
        return visitService.findByPatient(id);
    }
}
