package com.mediconnect.clinical.service;

import com.mediconnect.clinical.dto.ClinicalNoteRequest;
import com.mediconnect.clinical.entity.ClinicalNote;
import com.mediconnect.clinical.enums.NoteType;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.clinical.repository.ClinicalNoteRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClinicalNoteService {

    private final ClinicalNoteRepository noteRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public ClinicalNoteService(ClinicalNoteRepository noteRepository,
                                VisitRepository visitRepository,
                                UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<ClinicalNote> findByVisit(Long visitId) {
        return noteRepository.findByVisitIdOrderByCreatedOnDesc(visitId);
    }

    public Optional<ClinicalNote> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public ClinicalNote create(Long visitId, ClinicalNoteRequest request) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User author = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        ClinicalNote note = new ClinicalNote();
        note.setVisit(visit);
        note.setAuthor(author);
        note.setNoteType(request.getNoteType() != null ? NoteType.valueOf(request.getNoteType()) : NoteType.SOAP);
        note.setSubjective(request.getSubjective());
        note.setObjective(request.getObjective());
        note.setAssessment(request.getAssessment());
        note.setPlan(request.getPlan());
        note.setNoteContent(request.getNoteContent());
        note.setIsFinalized(false);

        ClinicalNote saved = noteRepository.save(note);
        log.info("Created clinical note {} for visit {}", saved.getId(), visitId);
        return saved;
    }

    @Transactional
    public ClinicalNote update(Long id, ClinicalNoteRequest request) {
        ClinicalNote note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical note not found with id: " + id));

        if (Boolean.TRUE.equals(note.getIsFinalized())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Finalized notes cannot be edited.");
        }

        note.setSubjective(request.getSubjective());
        note.setObjective(request.getObjective());
        note.setAssessment(request.getAssessment());
        note.setPlan(request.getPlan());
        note.setNoteContent(request.getNoteContent());
        if (request.getNoteType() != null) note.setNoteType(NoteType.valueOf(request.getNoteType()));

        return noteRepository.save(note);
    }

    @Transactional
    public ClinicalNote finalize(Long id) {
        ClinicalNote note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical note not found with id: " + id));

        if (Boolean.TRUE.equals(note.getIsFinalized())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Note is already finalized.");
        }

        note.setIsFinalized(true);
        note.setFinalizedOn(LocalDateTime.now());
        ClinicalNote finalized = noteRepository.save(note);
        log.info("Finalized clinical note {}", id);
        return finalized;
    }

    @Transactional
    public void delete(Long id) {
        ClinicalNote note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical note not found with id: " + id));

        if (Boolean.TRUE.equals(note.getIsFinalized())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Finalized notes cannot be deleted.");
        }

        noteRepository.deleteById(id);
    }
}
