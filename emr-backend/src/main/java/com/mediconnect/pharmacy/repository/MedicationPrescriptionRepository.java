package com.mediconnect.pharmacy.repository;

import com.mediconnect.pharmacy.entity.MedicationPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationPrescriptionRepository extends JpaRepository<MedicationPrescription, Long> {
    List<MedicationPrescription> findByPatientIdAndIsActiveTrue(Long patientId);
    List<MedicationPrescription> findByVisitId(Long visitId);
}
