package com.mediconnect.clinical.service;

import com.mediconnect.clinical.dto.VitalsRequest;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.clinical.entity.Vitals;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.clinical.repository.VisitRepository;
import com.mediconnect.clinical.repository.VitalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VitalsService {

    private final VitalsRepository vitalsRepository;
    private final VisitRepository visitRepository;

    public VitalsService(VitalsRepository vitalsRepository, VisitRepository visitRepository) {
        this.vitalsRepository = vitalsRepository;
        this.visitRepository = visitRepository;
    }

    public List<Vitals> findByVisit(Long visitId) {
        return vitalsRepository.findByVisitIdOrderByVitalsTakenOnDesc(visitId);
    }

    public Optional<Vitals> findById(Long id) {
        return vitalsRepository.findById(id);
    }

    @Transactional
    public Vitals create(Long visitId, VitalsRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Vitals vitals = new Vitals();
        vitals.setVisit(visit);
        applyRequest(vitals, request);
        if (vitals.getVitalsTakenOn() == null) {
            vitals.setVitalsTakenOn(LocalDateTime.now());
        }
        // Auto-calculate BMI if height and weight are provided
        if (vitals.getHeight() != null && vitals.getWeight() != null && vitals.getHeight() > 0) {
            double heightM = "cm".equalsIgnoreCase(vitals.getHeightUnit())
                    ? vitals.getHeight() / 100.0 : vitals.getHeight() * 0.0254;
            vitals.setBmi(Math.round((vitals.getWeight() / (heightM * heightM)) * 10.0) / 10.0);
        }
        return vitalsRepository.save(vitals);
    }

    @Transactional
    public Vitals update(Long id, VitalsRequest request) {
        Vitals vitals = vitalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vitals not found with id: " + id));
        applyRequest(vitals, request);
        return vitalsRepository.save(vitals);
    }

    @Transactional
    public void delete(Long id) {
        if (!vitalsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vitals not found with id: " + id);
        }
        vitalsRepository.deleteById(id);
    }

    private void applyRequest(Vitals v, VitalsRequest r) {
        v.setHeight(r.getHeight());
        v.setHeightUnit(r.getHeightUnit());
        v.setWeight(r.getWeight());
        v.setWeightUnit(r.getWeightUnit());
        if (r.getBmi() != null) v.setBmi(r.getBmi());
        v.setTemperature(r.getTemperature());
        v.setTemperatureUnit(r.getTemperatureUnit());
        v.setPulse(r.getPulse());
        v.setBpSystolic(r.getBpSystolic());
        v.setBpDiastolic(r.getBpDiastolic());
        v.setRespiratoryRate(r.getRespiratoryRate());
        v.setSpO2(r.getSpO2());
        v.setPainScale(r.getPainScale());
        v.setFreeNotes(r.getFreeNotes());
        v.setVitalsTakenOn(r.getVitalsTakenOn());
    }
}
