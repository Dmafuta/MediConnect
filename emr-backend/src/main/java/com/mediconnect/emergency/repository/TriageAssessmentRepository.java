package com.mediconnect.emergency.repository;

import com.mediconnect.emergency.entity.TriageAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TriageAssessmentRepository extends JpaRepository<TriageAssessment, Long> {
    Optional<TriageAssessment> findByVisitId(Long visitId);
    boolean existsByVisitId(Long visitId);
}
