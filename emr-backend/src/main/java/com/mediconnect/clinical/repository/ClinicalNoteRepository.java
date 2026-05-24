package com.mediconnect.clinical.repository;

import com.mediconnect.clinical.entity.ClinicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, Long> {
    List<ClinicalNote> findByVisitIdOrderByCreatedOnDesc(Long visitId);
    List<ClinicalNote> findByVisitIdAndNoteType(Long visitId, String noteType);
}
