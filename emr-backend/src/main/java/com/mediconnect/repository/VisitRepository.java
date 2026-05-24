package com.mediconnect.repository;

import com.mediconnect.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findAll(Pageable pageable);

    List<Visit> findByPatientId(Long patientId);

    Optional<Visit> findByVisitCode(String visitCode);

    List<Visit> findByProviderIdAndVisitDate(Long providerId, LocalDate date);

    List<Visit> findByVisitDateAndVisitStatus(LocalDate date, String visitStatus);
}
