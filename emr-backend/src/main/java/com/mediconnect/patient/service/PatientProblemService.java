package com.mediconnect.patient.service;

import com.mediconnect.patient.dto.PatientProblemRequest;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.patient.entity.PatientProblem;
import com.mediconnect.patient.enums.ProblemStatus;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.patient.repository.PatientProblemRepository;
import com.mediconnect.patient.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PatientProblemService {

    private final PatientProblemRepository problemRepository;
    private final PatientRepository patientRepository;

    public PatientProblemService(PatientProblemRepository problemRepository, PatientRepository patientRepository) {
        this.problemRepository = problemRepository;
        this.patientRepository = patientRepository;
    }

    public List<PatientProblem> findByPatient(Long patientId) {
        return problemRepository.findByPatientId(patientId);
    }

    public List<PatientProblem> findActiveByPatient(Long patientId) {
        return problemRepository.findByPatientIdAndIsResolvedFalse(patientId);
    }

    public Optional<PatientProblem> findById(Long id) {
        return problemRepository.findById(id);
    }

    @Transactional
    public PatientProblem create(Long patientId, PatientProblemRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        PatientProblem problem = new PatientProblem();
        problem.setPatient(patient);
        applyRequest(problem, request);
        return problemRepository.save(problem);
    }

    @Transactional
    public PatientProblem update(Long id, PatientProblemRequest request) {
        PatientProblem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + id));
        applyRequest(problem, request);
        return problemRepository.save(problem);
    }

    @Transactional
    public void delete(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with id: " + id);
        }
        problemRepository.deleteById(id);
    }

    private void applyRequest(PatientProblem p, PatientProblemRequest r) {
        p.setProblemDescription(r.getProblemDescription());
        if (r.getCurrentStatus() != null) p.setCurrentStatus(ProblemStatus.valueOf(r.getCurrentStatus()));
        p.setNote(r.getNote());
        p.setOnsetDate(r.getOnsetDate());
        p.setResolvedDate(r.getResolvedDate());
        if (r.getIsResolved() != null) p.setIsResolved(r.getIsResolved());
        if (r.getIsPrincipalProblem() != null) p.setIsPrincipalProblem(r.getIsPrincipalProblem());
    }
}
