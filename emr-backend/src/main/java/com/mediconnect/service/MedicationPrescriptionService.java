package com.mediconnect.service;

import com.mediconnect.dto.MedicationPrescriptionRequest;
import com.mediconnect.entity.MedicationPrescription;
import com.mediconnect.entity.Patient;
import com.mediconnect.entity.User;
import com.mediconnect.entity.Visit;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.MedicationPrescriptionRepository;
import com.mediconnect.repository.PatientRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.repository.VisitRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationPrescriptionService {

    private final MedicationPrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public MedicationPrescriptionService(MedicationPrescriptionRepository prescriptionRepository,
                                          PatientRepository patientRepository,
                                          UserRepository userRepository,
                                          VisitRepository visitRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    public List<MedicationPrescription> findByPatient(Long patientId) {
        return prescriptionRepository.findByPatientIdAndIsActiveTrue(patientId);
    }

    public List<MedicationPrescription> findByVisit(Long visitId) {
        return prescriptionRepository.findByVisitId(visitId);
    }

    public Optional<MedicationPrescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Transactional
    public MedicationPrescription create(MedicationPrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User prescriber = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        MedicationPrescription prescription = new MedicationPrescription();
        prescription.setPatient(patient);
        prescription.setPrescribedBy(prescriber);
        prescription.setMedicationName(request.getMedicationName());
        prescription.setRoute(request.getRoute());
        prescription.setDose(request.getDose());
        prescription.setFrequency(request.getFrequency());
        prescription.setDuration(request.getDuration());
        prescription.setDurationType(request.getDurationType());
        prescription.setRefill(request.getRefill());
        prescription.setTypeOfMedication(request.getTypeOfMedication());
        prescription.setInstructions(request.getInstructions());
        prescription.setIsActive(true);

        if (request.getVisitId() != null) {
            Visit visit = visitRepository.findById(request.getVisitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));
            prescription.setVisit(visit);
        }

        return prescriptionRepository.save(prescription);
    }

    @Transactional
    public void delete(Long id) {
        MedicationPrescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescription.setIsActive(false);
        prescriptionRepository.save(prescription);
    }
}
