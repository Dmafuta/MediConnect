package com.mediconnect.emergency.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.emergency.enums.TriageDisposition;
import com.mediconnect.clinical.entity.Visit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "triage_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TriageAssessment extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    // ESI: 1=Immediate, 2=Emergent, 3=Urgent, 4=Less Urgent, 5=Non-Urgent
    @Column(name = "esi_level")
    private Integer esiLevel;

    @Column(name = "chief_complaint", nullable = false, columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(name = "history_of_present_illness", columnDefinition = "TEXT")
    private String historyOfPresentIllness;

    // e.g., "2 hours", "3 days"
    @Column(name = "onset_duration")
    private String onsetDuration;

    // sudden, gradual, intermittent
    @Column(name = "onset_character")
    private String onsetCharacter;

    // Alert, Distressed, Anxious, Lethargic, Unresponsive
    @Column(name = "general_appearance")
    private String generalAppearance;

    // Alert and Oriented, Confused, Lethargic, Unresponsive
    @Column(name = "mental_status")
    private String mentalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "disposition")
    private TriageDisposition disposition = TriageDisposition.WAITING_ROOM;

    @Column(name = "triage_completed_at")
    private LocalDateTime triageCompletedAt;

    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;
}
