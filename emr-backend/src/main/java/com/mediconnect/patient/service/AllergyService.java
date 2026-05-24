package com.mediconnect.patient.service;

import com.mediconnect.patient.dto.AllergyRequest;
import com.mediconnect.patient.entity.Allergy;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.patient.repository.AllergyRepository;
import com.mediconnect.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AllergyService {

    private final AllergyRepository allergyRepository;
    private final PatientRepository patientRepository;

    public AllergyService(AllergyRepository allergyRepository, PatientRepository patientRepository) {
        this.allergyRepository = allergyRepository;
        this.patientRepository = patientRepository;
    }

    public List<Allergy> findByPatient(Long patientId) {
        return allergyRepository.findByPatientIdAndIsActiveTrue(patientId);
    }

    public Optional<Allergy> findById(Long id) {
        return allergyRepository.findById(id);
    }

    @Transactional
    public Allergy create(Long patientId, AllergyRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        Allergy allergy = new Allergy();
        allergy.setPatient(patient);
        allergy.setAllergenName(request.getAllergenName());
        allergy.setAllergyType(request.getAllergyType());
        allergy.setSeverity(request.getSeverity());
        allergy.setVerified(Boolean.TRUE.equals(request.getVerified()));
        allergy.setReaction(request.getReaction());
        allergy.setComments(request.getComments());
        allergy.setIsActive(true);
        return allergyRepository.save(allergy);
    }

    @Transactional
    public Allergy update(Long id, AllergyRequest request) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + id));
        allergy.setAllergenName(request.getAllergenName());
        allergy.setAllergyType(request.getAllergyType());
        allergy.setSeverity(request.getSeverity());
        if (request.getVerified() != null) allergy.setVerified(request.getVerified());
        allergy.setReaction(request.getReaction());
        allergy.setComments(request.getComments());
        return allergyRepository.save(allergy);
    }

    @Transactional
    public void delete(Long id) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + id));
        allergy.setIsActive(false);
        allergyRepository.save(allergy);
    }
}
