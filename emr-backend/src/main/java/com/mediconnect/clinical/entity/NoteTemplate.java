package com.mediconnect.clinical.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "note_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class NoteTemplate extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    // OPD, EMERGENCY, FOLLOW_UP, ROUTINE, NURSING, PROCEDURE
    @Column(name = "visit_type")
    private String visitType;

    // SOAP, PROGRESS, DISCHARGE, NURSING, REFERRAL, OTHER
    @Column(name = "note_type", nullable = false)
    private String noteType = "SOAP";

    @Column(name = "subjective_template", columnDefinition = "TEXT")
    private String subjectiveTemplate;

    @Column(name = "objective_template", columnDefinition = "TEXT")
    private String objectiveTemplate;

    @Column(name = "assessment_template", columnDefinition = "TEXT")
    private String assessmentTemplate;

    @Column(name = "plan_template", columnDefinition = "TEXT")
    private String planTemplate;

    @Column(name = "content_template", columnDefinition = "TEXT")
    private String contentTemplate;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE")
    private Boolean isActive = true;
}
