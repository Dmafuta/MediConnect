package com.mediconnect.pharmacy.service;

import com.mediconnect.pharmacy.dto.MedicationPrescriptionRequest;
import com.mediconnect.pharmacy.dto.PrescriptionCreateResponse;
import com.mediconnect.patient.entity.Allergy;
import com.mediconnect.pharmacy.entity.MedicationPrescription;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.patient.repository.AllergyRepository;
import com.mediconnect.pharmacy.repository.MedicationPrescriptionRepository;
import com.mediconnect.patient.repository.PatientRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MedicationPrescriptionService {

    private final MedicationPrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;
    private final AllergyRepository allergyRepository;

    public MedicationPrescriptionService(MedicationPrescriptionRepository prescriptionRepository,
                                          PatientRepository patientRepository,
                                          UserRepository userRepository,
                                          VisitRepository visitRepository,
                                          AllergyRepository allergyRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
        this.allergyRepository = allergyRepository;
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
    public PrescriptionCreateResponse create(MedicationPrescriptionRequest request) {
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

        MedicationPrescription saved = prescriptionRepository.save(prescription);
        log.info("Created prescription {} for patient {}", saved.getId(), patient.getId());

        // Drug-allergy interaction check — soft alert, does not block save
        List<String> alerts = checkDrugAllergyInteractions(patient, request.getMedicationName());
        if (!alerts.isEmpty()) {
            log.warn("Drug-allergy alerts for prescription {}: {}", saved.getId(), alerts);
        }

        return new PrescriptionCreateResponse(saved, alerts);
    }

    @Transactional
    public void delete(Long id) {
        MedicationPrescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescription.setIsActive(false);
        prescriptionRepository.save(prescription);
    }

    private List<String> checkDrugAllergyInteractions(Patient patient, String medicationName) {
        List<Allergy> allergies = allergyRepository.findByPatientIdAndIsActiveTrue(patient.getId());
        List<String> alerts = new ArrayList<>();
        String medLower = medicationName.toLowerCase();

        for (Allergy allergy : allergies) {
            if (allergy.getAllergenName() == null) continue;
            String allergenLower = allergy.getAllergenName().toLowerCase();

            if (medLower.contains(allergenLower) || allergenLower.contains(medLower)) {
                // Substring match — cast wide net; clinical review is still required
                String severity = allergy.getSeverity() != null ? " [" + allergy.getSeverity().name() + "]" : "";
                String type = allergy.getAllergyType() != null ? allergy.getAllergyType().name() : "UNKNOWN";
                alerts.add("ALLERGY ALERT: Patient has a documented " + type
                        + " allergy to " + allergy.getAllergenName() + severity
                        + ". Verify before dispensing.");
            }
        }
        return alerts;
    }
}
