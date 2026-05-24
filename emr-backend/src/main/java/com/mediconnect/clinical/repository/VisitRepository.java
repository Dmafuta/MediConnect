package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    long countByVisitDate(LocalDate date);

    @Query("SELECT v.visitDate, COUNT(v) FROM Visit v WHERE v.visitDate BETWEEN :from AND :to GROUP BY v.visitDate ORDER BY v.visitDate ASC")
    List<Object[]> countByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
