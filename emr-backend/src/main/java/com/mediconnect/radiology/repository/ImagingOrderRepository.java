package com.mediconnect.radiology.repository;

import com.mediconnect.radiology.entity.ImagingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagingOrderRepository extends JpaRepository<ImagingOrder, Long> {
    List<ImagingOrder> findByVisitId(Long visitId);
    List<ImagingOrder> findByPatientId(Long patientId);
    List<ImagingOrder> findByStatus(String status);
    long countByStatus(String status);
}
