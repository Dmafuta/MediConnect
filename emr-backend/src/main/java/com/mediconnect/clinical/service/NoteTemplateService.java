package com.mediconnect.clinical.service;

import com.mediconnect.clinical.dto.NoteTemplateRequest;
import com.mediconnect.clinical.entity.NoteTemplate;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.clinical.repository.NoteTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NoteTemplateService {

    private final NoteTemplateRepository templateRepository;

    public NoteTemplateService(NoteTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<NoteTemplate> findAll() {
        return templateRepository.findByIsActiveTrue();
    }

    public List<NoteTemplate> findByVisitType(String visitType) {
        return templateRepository.findByVisitTypeAndIsActiveTrue(visitType);
    }

    public List<NoteTemplate> findByNoteType(String noteType) {
        return templateRepository.findByNoteTypeAndIsActiveTrue(noteType);
    }

    public Optional<NoteTemplate> findById(Long id) {
        return templateRepository.findById(id);
    }

    @Transactional
    public NoteTemplate create(NoteTemplateRequest request) {
        NoteTemplate template = new NoteTemplate();
        applyRequest(template, request);
        template.setIsActive(true);
        return templateRepository.save(template);
    }

    @Transactional
    public NoteTemplate update(Long id, NoteTemplateRequest request) {
        NoteTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note template not found with id: " + id));
        applyRequest(template, request);
        return templateRepository.save(template);
    }

    @Transactional
    public void delete(Long id) {
        NoteTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note template not found with id: " + id));
        template.setIsActive(false);
        templateRepository.save(template);
    }

    private void applyRequest(NoteTemplate t, NoteTemplateRequest r) {
        t.setTemplateName(r.getTemplateName());
        t.setVisitType(r.getVisitType());
        t.setNoteType(r.getNoteType() != null ? r.getNoteType() : "SOAP");
        t.setSubjectiveTemplate(r.getSubjectiveTemplate());
        t.setObjectiveTemplate(r.getObjectiveTemplate());
        t.setAssessmentTemplate(r.getAssessmentTemplate());
        t.setPlanTemplate(r.getPlanTemplate());
        t.setContentTemplate(r.getContentTemplate());
    }
}
