package com.mediconnect.repository;

import com.mediconnect.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByVisitIdAndIsActiveTrue(Long visitId);
    List<Diagnosis> findByPatientIdAndIsActiveTrue(Long patientId);
}
