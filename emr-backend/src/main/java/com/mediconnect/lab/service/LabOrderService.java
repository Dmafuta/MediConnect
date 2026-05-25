package com.mediconnect.lab.service;

import com.mediconnect.lab.dto.LabOrderRequest;
import com.mediconnect.lab.dto.LabResultRequest;
import com.mediconnect.lab.enums.LabOrderStatus;
import com.mediconnect.shared.dto.OrderStatusRequest;
import com.mediconnect.shared.enums.OrderPriority;
import com.mediconnect.lab.entity.LabOrder;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.lab.repository.LabOrderRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public LabOrderService(LabOrderRepository labOrderRepository,
                            VisitRepository visitRepository,
                            UserRepository userRepository) {
        this.labOrderRepository = labOrderRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<LabOrder> findByVisit(Long visitId) {
        return labOrderRepository.findByVisitId(visitId);
    }

    public List<LabOrder> findByPatient(Long patientId) {
        return labOrderRepository.findByPatientId(patientId);
    }

    public List<LabOrder> findByStatus(LabOrderStatus status) {
        return labOrderRepository.findByStatus(status);
    }

    public Optional<LabOrder> findById(Long id) {
        return labOrderRepository.findById(id);
    }

    @Transactional
    public LabOrder create(Long visitId, LabOrderRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User orderedBy = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        LabOrder order = new LabOrder();
        order.setVisit(visit);
        order.setPatient(visit.getPatient());
        order.setOrderedBy(orderedBy);
        order.setTestName(request.getTestName());
        order.setTestCode(request.getTestCode());
        order.setPriority(request.getPriority() != null ? OrderPriority.valueOf(request.getPriority()) : OrderPriority.ROUTINE);
        order.setClinicalIndication(request.getClinicalIndication());
        order.setNotes(request.getNotes());
        order.setStatus(LabOrderStatus.ORDERED);

        LabOrder saved = labOrderRepository.save(order);
        log.info("Created lab order {} for visit {}", saved.getId(), visitId);
        return saved;
    }

    @Transactional
    public LabOrder updateStatus(Long id, OrderStatusRequest request) {
        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab order not found with id: " + id));
        order.setStatus(LabOrderStatus.valueOf(request.getStatus()));
        if (request.getNotes() != null) order.setNotes(request.getNotes());
        return labOrderRepository.save(order);
    }

    @Transactional
    public LabOrder recordResult(Long id, LabResultRequest request) {
        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab order not found with id: " + id));
        order.setResultValue(request.getResultValue());
        order.setResultUnit(request.getResultUnit());
        order.setResultRange(request.getResultRange());
        order.setResultInterpretation(request.getResultInterpretation());
        order.setResultDate(request.getResultDate() != null ? request.getResultDate() : LocalDateTime.now());
        order.setStatus(LabOrderStatus.RESULTED);
        log.info("Recorded result for lab order {}", id);
        return labOrderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        LabOrder order = labOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab order not found with id: " + id));
        order.setStatus(LabOrderStatus.CANCELLED);
        labOrderRepository.save(order);
    }
}
