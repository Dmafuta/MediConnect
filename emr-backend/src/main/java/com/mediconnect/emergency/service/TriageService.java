package com.mediconnect.emergency.service;

import com.mediconnect.emergency.dto.TriageAssessmentRequest;
import com.mediconnect.emergency.entity.TriageAssessment;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.emergency.repository.TriageAssessmentRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TriageService {

    private final TriageAssessmentRepository triageRepository;
    private final VisitRepository visitRepository;

    public TriageService(TriageAssessmentRepository triageRepository, VisitRepository visitRepository) {
        this.triageRepository = triageRepository;
        this.visitRepository = visitRepository;
    }

    public Optional<TriageAssessment> findByVisit(Long visitId) {
        return triageRepository.findByVisitId(visitId);
    }

    @Transactional
    public TriageAssessment create(Long visitId, TriageAssessmentRequest request) {
        if (triageRepository.existsByVisitId(visitId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Triage assessment already exists for this visit. Use PUT to update.");
        }

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        TriageAssessment triage = new TriageAssessment();
        triage.setVisit(visit);
        applyRequest(triage, request);
        triage.setTriageCompletedAt(LocalDateTime.now());

        // Mark the visit as triaged and update disposition in queue
        visit.setIsTriaged(true);
        if (request.getDisposition() != null) {
            visit.setQueueStatus(mapDispositionToQueueStatus(request.getDisposition()));
        }
        visitRepository.save(visit);

        return triageRepository.save(triage);
    }

    @Transactional
    public TriageAssessment update(Long visitId, TriageAssessmentRequest request) {
        TriageAssessment triage = triageRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("No triage assessment found for visit id: " + visitId));
        applyRequest(triage, request);
        return triageRepository.save(triage);
    }

    private void applyRequest(TriageAssessment t, TriageAssessmentRequest r) {
        t.setChiefComplaint(r.getChiefComplaint());
        t.setHistoryOfPresentIllness(r.getHistoryOfPresentIllness());
        t.setEsiLevel(r.getEsiLevel());
        t.setOnsetDuration(r.getOnsetDuration());
        t.setOnsetCharacter(r.getOnsetCharacter());
        t.setGeneralAppearance(r.getGeneralAppearance());
        t.setMentalStatus(r.getMentalStatus());
        if (r.getDisposition() != null) t.setDisposition(r.getDisposition());
        t.setAdditionalNotes(r.getAdditionalNotes());
    }

    private String mapDispositionToQueueStatus(String disposition) {
        return switch (disposition) {
            case "TREATMENT_ROOM", "OBSERVATION", "ICU" -> "WITH_PROVIDER";
            case "DISCHARGE_HOME", "TRANSFER_OUT"       -> "DONE";
            default                                      -> "WAITING";
        };
    }
}
