package com.mediconnect.nursing.repository;

import com.mediconnect.nursing.entity.MedicationAdministration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationAdministrationRepository extends JpaRepository<MedicationAdministration, Long> {
    List<MedicationAdministration> findByVisitIdOrderByAdministeredAtDesc(Long visitId);
    List<MedicationAdministration> findByPatientIdOrderByAdministeredAtDesc(Long patientId);
    List<MedicationAdministration> findByPrescriptionId(Long prescriptionId);
}
