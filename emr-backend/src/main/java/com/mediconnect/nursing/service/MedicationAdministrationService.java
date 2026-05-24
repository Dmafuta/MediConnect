package com.mediconnect.nursing.service;

import com.mediconnect.nursing.dto.MedicationAdministrationRequest;
import com.mediconnect.nursing.entity.MedicationAdministration;
import com.mediconnect.pharmacy.entity.MedicationPrescription;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.nursing.repository.MedicationAdministrationRepository;
import com.mediconnect.pharmacy.repository.MedicationPrescriptionRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationAdministrationService {

    private final MedicationAdministrationRepository marRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final MedicationPrescriptionRepository prescriptionRepository;

    public MedicationAdministrationService(MedicationAdministrationRepository marRepository,
                                            VisitRepository visitRepository,
                                            UserRepository userRepository,
                                            MedicationPrescriptionRepository prescriptionRepository) {
        this.marRepository = marRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<MedicationAdministration> findByVisit(Long visitId) {
        return marRepository.findByVisitIdOrderByAdministeredAtDesc(visitId);
    }

    public List<MedicationAdministration> findByPatient(Long patientId) {
        return marRepository.findByPatientIdOrderByAdministeredAtDesc(patientId);
    }

    public Optional<MedicationAdministration> findById(Long id) {
        return marRepository.findById(id);
    }

    @Transactional
    public MedicationAdministration record(Long visitId, MedicationAdministrationRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User administeredBy = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        MedicationAdministration admin = new MedicationAdministration();
        admin.setPatient(visit.getPatient());
        admin.setVisit(visit);
        admin.setAdministeredBy(administeredBy);
        admin.setMedicationName(request.getMedicationName());
        admin.setDose(request.getDose());
        admin.setRoute(request.getRoute());
        admin.setAdministeredAt(request.getAdministeredAt());
        admin.setStatus(request.getStatus() != null ? request.getStatus() : "ADMINISTERED");
        admin.setHoldReason(request.getHoldReason());
        admin.setNotes(request.getNotes());

        if (request.getPrescriptionId() != null) {
            MedicationPrescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + request.getPrescriptionId()));
            admin.setPrescription(prescription);
            // Pre-fill medication name from prescription if not overridden
            if (admin.getMedicationName() == null || admin.getMedicationName().isBlank()) {
                admin.setMedicationName(prescription.getMedicationName());
            }
        }

        return marRepository.save(admin);
    }

    @Transactional
    public void delete(Long id) {
        if (!marRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medication administration record not found with id: " + id);
        }
        marRepository.deleteById(id);
    }
}
