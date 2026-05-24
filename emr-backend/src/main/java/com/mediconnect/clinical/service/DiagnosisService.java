package com.mediconnect.clinical.service;

import com.mediconnect.clinical.dto.DiagnosisRequest;
import com.mediconnect.clinical.entity.Diagnosis;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.clinical.repository.DiagnosisRepository;
import com.mediconnect.patient.repository.PatientRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository, VisitRepository visitRepository,
                             PatientRepository patientRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
    }

    public List<Diagnosis> findByVisit(Long visitId) {
        return diagnosisRepository.findByVisitIdAndIsActiveTrue(visitId);
    }

    public List<Diagnosis> findByPatient(Long patientId) {
        return diagnosisRepository.findByPatientIdAndIsActiveTrue(patientId);
    }

    public Optional<Diagnosis> findById(Long id) {
        return diagnosisRepository.findById(id);
    }

    @Transactional
    public Diagnosis create(Long visitId, DiagnosisRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setVisit(visit);
        diagnosis.setPatient(visit.getPatient());
        diagnosis.setIcd10Code(request.getIcd10Code());
        diagnosis.setIcd10Description(request.getIcd10Description());
        diagnosis.setDiagnosisType(request.getDiagnosisType() != null ? request.getDiagnosisType() : "PRIMARY");
        diagnosis.setIsActive(true);
        return diagnosisRepository.save(diagnosis);
    }

    @Transactional
    public Diagnosis update(Long id, DiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + id));
        diagnosis.setIcd10Code(request.getIcd10Code());
        diagnosis.setIcd10Description(request.getIcd10Description());
        if (request.getDiagnosisType() != null) diagnosis.setDiagnosisType(request.getDiagnosisType());
        return diagnosisRepository.save(diagnosis);
    }

    @Transactional
    public void delete(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + id));
        diagnosis.setIsActive(false);
        diagnosisRepository.save(diagnosis);
    }
}
