package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByVisitIdAndIsActiveTrue(Long visitId);
    List<Diagnosis> findByPatientIdAndIsActiveTrue(Long patientId);

    @Query(value = "SELECT d.icd10_description, COUNT(d) FROM patient_diagnoses d " +
                   "WHERE d.is_active = true AND d.created_on BETWEEN :from AND :to " +
                   "GROUP BY d.icd10_description ORDER BY COUNT(d) DESC LIMIT :limit",
           nativeQuery = true)
    List<Object[]> countByDescriptionInRange(@Param("from") LocalDate from,
                                              @Param("to") LocalDate to,
                                              @Param("limit") int limit);
}
