package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.NoteTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteTemplateRepository extends JpaRepository<NoteTemplate, Long> {
    List<NoteTemplate> findByIsActiveTrue();
    List<NoteTemplate> findByVisitTypeAndIsActiveTrue(String visitType);
    List<NoteTemplate> findByNoteTypeAndIsActiveTrue(String noteType);
}
