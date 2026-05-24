package com.mediconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "clinical_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ClinicalNote extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // SOAP, PROGRESS, DISCHARGE, REFERRAL, OTHER
    @Column(name = "note_type", nullable = false)
    private String noteType = "SOAP";

    // SOAP fields — used when noteType = SOAP
    @Column(name = "subjective", columnDefinition = "TEXT")
    private String subjective;

    @Column(name = "objective", columnDefinition = "TEXT")
    private String objective;

    @Column(name = "assessment", columnDefinition = "TEXT")
    private String assessment;

    @Column(name = "plan", columnDefinition = "TEXT")
    private String plan;

    // Free-text content for non-SOAP note types
    @Column(name = "note_content", columnDefinition = "TEXT")
    private String noteContent;

    // Once finalized a note is read-only
    @Column(name = "is_finalized")
    private Boolean isFinalized = false;

    @Column(name = "finalized_on")
    private LocalDateTime finalizedOn;
}
