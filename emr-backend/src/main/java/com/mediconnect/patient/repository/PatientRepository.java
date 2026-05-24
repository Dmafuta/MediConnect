package com.mediconnect.patient.repository;

import com.mediconnect.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByIsActiveTrue(Pageable pageable);

    Optional<Patient> findByMrn(String mrn);

    Optional<Patient> findByContactNumber(String contactNumber);

    Optional<Patient> findByEmail(String email);

    long countByIsActiveTrue();

    @Query("SELECT p FROM Patient p WHERE p.isActive = true AND (" +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.mrn) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "p.contactNumber LIKE CONCAT('%', :q, '%'))")
    Page<Patient> search(@Param("q") String query, Pageable pageable);
}
