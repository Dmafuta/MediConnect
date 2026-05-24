package com.mediconnect.radiology.service;

import com.mediconnect.radiology.dto.ImagingOrderRequest;
import com.mediconnect.shared.dto.OrderStatusRequest;
import com.mediconnect.radiology.entity.ImagingOrder;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.radiology.repository.ImagingOrderRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImagingOrderService {

    private final ImagingOrderRepository imagingOrderRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public ImagingOrderService(ImagingOrderRepository imagingOrderRepository,
                                VisitRepository visitRepository,
                                UserRepository userRepository) {
        this.imagingOrderRepository = imagingOrderRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<ImagingOrder> findByVisit(Long visitId) {
        return imagingOrderRepository.findByVisitId(visitId);
    }

    public List<ImagingOrder> findByPatient(Long patientId) {
        return imagingOrderRepository.findByPatientId(patientId);
    }

    public Optional<ImagingOrder> findById(Long id) {
        return imagingOrderRepository.findById(id);
    }

    @Transactional
    public ImagingOrder create(Long visitId, ImagingOrderRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User orderedBy = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        ImagingOrder order = new ImagingOrder();
        order.setVisit(visit);
        order.setPatient(visit.getPatient());
        order.setOrderedBy(orderedBy);
        order.setStudyType(request.getStudyType());
        order.setBodyPart(request.getBodyPart());
        order.setPriority(request.getPriority() != null ? request.getPriority() : "ROUTINE");
        order.setClinicalIndication(request.getClinicalIndication());
        order.setNotes(request.getNotes());
        order.setStatus("ORDERED");

        return imagingOrderRepository.save(order);
    }

    @Transactional
    public ImagingOrder updateStatus(Long id, OrderStatusRequest request) {
        ImagingOrder order = imagingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imaging order not found with id: " + id));
        order.setStatus(request.getStatus());
        if (request.getNotes() != null) order.setNotes(request.getNotes());
        return imagingOrderRepository.save(order);
    }

    @Transactional
    public ImagingOrder recordReport(Long id, String report) {
        ImagingOrder order = imagingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imaging order not found with id: " + id));
        order.setReport(report);
        order.setReportDate(LocalDateTime.now());
        order.setStatus("REPORTED");
        return imagingOrderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        ImagingOrder order = imagingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imaging order not found with id: " + id));
        order.setStatus("CANCELLED");
        imagingOrderRepository.save(order);
    }
}
