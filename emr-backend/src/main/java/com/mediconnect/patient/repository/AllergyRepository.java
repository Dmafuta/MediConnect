package com.mediconnect.patient.repository;

import com.mediconnect.patient.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    List<Allergy> findByPatientIdAndIsActiveTrue(Long patientId);
}
