package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals, Long> {
    List<Vitals> findByVisitIdOrderByVitalsTakenOnDesc(Long visitId);
}
