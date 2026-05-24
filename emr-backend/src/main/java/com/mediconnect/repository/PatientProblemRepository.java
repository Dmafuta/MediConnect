package com.mediconnect.repository;

import com.mediconnect.entity.PatientProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientProblemRepository extends JpaRepository<PatientProblem, Long> {
    List<PatientProblem> findByPatientId(Long patientId);
    List<PatientProblem> findByPatientIdAndIsResolvedFalse(Long patientId);
}
