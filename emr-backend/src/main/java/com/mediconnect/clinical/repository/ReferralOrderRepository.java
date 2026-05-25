package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.ReferralOrder;
import com.mediconnect.clinical.enums.ReferralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralOrderRepository extends JpaRepository<ReferralOrder, Long> {
    List<ReferralOrder> findByVisitId(Long visitId);
    List<ReferralOrder> findByPatientId(Long patientId);
    List<ReferralOrder> findByStatus(ReferralStatus status);
    long countByStatus(ReferralStatus status);
}
