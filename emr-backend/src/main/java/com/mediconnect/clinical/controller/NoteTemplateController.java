package com.mediconnect.clinical.controller;

import com.mediconnect.clinical.dto.NoteTemplateRequest;
import com.mediconnect.clinical.entity.NoteTemplate;
import com.mediconnect.clinical.service.NoteTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note-templates")
public class NoteTemplateController {

    private final NoteTemplateService templateService;

    public NoteTemplateController(NoteTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<NoteTemplate> getAll(@RequestParam(required = false) String visitType,
                                      @RequestParam(required = false) String noteType) {
        if (visitType != null) return templateService.findByVisitType(visitType);
        if (noteType != null)  return templateService.findByNoteType(noteType);
        return templateService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public ResponseEntity<NoteTemplate> getById(@PathVariable Long id) {
        return templateService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public NoteTemplate create(@Valid @RequestBody NoteTemplateRequest request) {
        return templateService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<NoteTemplate> update(@PathVariable Long id,
                                                @Valid @RequestBody NoteTemplateRequest request) {
        return ResponseEntity.ok(templateService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('System Admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
