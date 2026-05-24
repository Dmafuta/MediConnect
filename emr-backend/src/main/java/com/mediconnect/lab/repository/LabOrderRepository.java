package com.mediconnect.lab.repository;

import com.mediconnect.lab.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    List<LabOrder> findByVisitId(Long visitId);
    List<LabOrder> findByPatientId(Long patientId);
    List<LabOrder> findByStatus(String status);
    long countByStatus(String status);

    @Query("SELECT l.status, COUNT(l) FROM LabOrder l WHERE CAST(l.createdOn AS date) BETWEEN :from AND :to GROUP BY l.status")
    List<Object[]> countByStatusInRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
