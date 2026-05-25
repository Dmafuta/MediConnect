package com.mediconnect.patient.service;

import com.mediconnect.patient.dto.PatientCreateRequest;
import com.mediconnect.patient.dto.PatientUpdateRequest;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.patient.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Page<Patient> findAllPatients(Pageable pageable) {
        return patientRepository.findByIsActiveTrue(pageable);
    }

    public Page<Patient> searchPatients(String query, Pageable pageable) {
        return patientRepository.search(query, pageable);
    }

    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id).filter(p -> Boolean.TRUE.equals(p.getIsActive()));
    }

    public Optional<Patient> findPatientByMrn(String mrn) {
        return patientRepository.findByMrn(mrn);
    }

    @Transactional
    public Patient createPatient(PatientCreateRequest request) {
        Patient patient = new Patient();
        applyCreateRequest(patient, request);
        patient.setIsActive(true);
        patient = patientRepository.save(patient);
        // Generate MRN after we have the DB-assigned ID
        patient.setMrn("MC-" + String.format("%06d", patient.getId()));
        patient = patientRepository.save(patient);
        log.info("Created patient {} with MRN {}", patient.getId(), patient.getMrn());
        return patient;
    }

    @Transactional
    public Patient updatePatient(Long id, PatientUpdateRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        applyUpdateRequest(patient, request);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patient.setIsActive(false);
        patientRepository.save(patient);
    }

    private void applyCreateRequest(Patient patient, PatientCreateRequest r) {
        patient.setSalutation(r.getSalutation());
        patient.setFirstName(r.getFirstName());
        patient.setMiddleName(r.getMiddleName());
        patient.setLastName(r.getLastName());
        patient.setDateOfBirth(r.getDateOfBirth());
        patient.setGender(r.getGender());
        patient.setBloodGroup(r.getBloodGroup());
        patient.setMaritalStatus(r.getMaritalStatus());
        patient.setContactNumber(r.getContactNumber());
        patient.setAlternatePhone(r.getAlternatePhone());
        patient.setEmail(r.getEmail());
        patient.setAddress(r.getAddress());
        patient.setEmergencyContactName(r.getEmergencyContactName());
        patient.setEmergencyContactRelation(r.getEmergencyContactRelation());
        patient.setEmergencyContactNumber(r.getEmergencyContactNumber());
    }

    private void applyUpdateRequest(Patient patient, PatientUpdateRequest r) {
        patient.setSalutation(r.getSalutation());
        patient.setFirstName(r.getFirstName());
        patient.setMiddleName(r.getMiddleName());
        patient.setLastName(r.getLastName());
        patient.setDateOfBirth(r.getDateOfBirth());
        patient.setGender(r.getGender());
        patient.setBloodGroup(r.getBloodGroup());
        patient.setMaritalStatus(r.getMaritalStatus());
        if (r.getContactNumber() != null) patient.setContactNumber(r.getContactNumber());
        patient.setAlternatePhone(r.getAlternatePhone());
        if (r.getEmail() != null) patient.setEmail(r.getEmail());
        patient.setAddress(r.getAddress());
        patient.setEmergencyContactName(r.getEmergencyContactName());
        patient.setEmergencyContactRelation(r.getEmergencyContactRelation());
        patient.setEmergencyContactNumber(r.getEmergencyContactNumber());
    }
}
