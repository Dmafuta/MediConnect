package com.mediconnect.clinical.service;

import com.mediconnect.shared.dto.OrderStatusRequest;
import com.mediconnect.clinical.dto.ReferralOrderRequest;
import com.mediconnect.clinical.entity.ReferralOrder;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.clinical.repository.ReferralOrderRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReferralOrderService {

    private final ReferralOrderRepository referralOrderRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public ReferralOrderService(ReferralOrderRepository referralOrderRepository,
                                 VisitRepository visitRepository,
                                 UserRepository userRepository) {
        this.referralOrderRepository = referralOrderRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<ReferralOrder> findByVisit(Long visitId) {
        return referralOrderRepository.findByVisitId(visitId);
    }

    public List<ReferralOrder> findByPatient(Long patientId) {
        return referralOrderRepository.findByPatientId(patientId);
    }

    public Optional<ReferralOrder> findById(Long id) {
        return referralOrderRepository.findById(id);
    }

    @Transactional
    public ReferralOrder create(Long visitId, ReferralOrderRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User referredBy = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        ReferralOrder order = new ReferralOrder();
        order.setVisit(visit);
        order.setPatient(visit.getPatient());
        order.setReferredBy(referredBy);
        order.setReferredToSpecialty(request.getReferredToSpecialty());
        order.setReferredToProvider(request.getReferredToProvider());
        order.setPriority(request.getPriority() != null ? request.getPriority() : "ROUTINE");
        order.setReasonForReferral(request.getReasonForReferral());
        order.setNotes(request.getNotes());
        order.setStatus("PENDING");

        return referralOrderRepository.save(order);
    }

    @Transactional
    public ReferralOrder updateStatus(Long id, OrderStatusRequest request) {
        ReferralOrder order = referralOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Referral order not found with id: " + id));
        order.setStatus(request.getStatus());
        if (request.getNotes() != null) order.setNotes(request.getNotes());
        return referralOrderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        ReferralOrder order = referralOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Referral order not found with id: " + id));
        order.setStatus("CANCELLED");
        referralOrderRepository.save(order);
    }
}
