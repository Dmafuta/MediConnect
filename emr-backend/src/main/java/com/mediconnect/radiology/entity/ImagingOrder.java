package com.mediconnect.radiology.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "imaging_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ImagingOrder extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_by_id", nullable = false)
    private User orderedBy;

    // X_RAY, CT, MRI, ULTRASOUND, PET_CT, NUCLEAR, FLUOROSCOPY, OTHER
    @Column(name = "study_type", nullable = false)
    private String studyType;

    @Column(name = "body_part")
    private String bodyPart;

    // ROUTINE, URGENT, STAT
    @Column(name = "priority", nullable = false)
    private String priority = "ROUTINE";

    // ORDERED, SCHEDULED, IN_PROGRESS, COMPLETED, REPORTED, CANCELLED
    @Column(name = "status", nullable = false)
    private String status = "ORDERED";

    @Column(name = "clinical_indication", columnDefinition = "TEXT")
    private String clinicalIndication;

    @Column(name = "report", columnDefinition = "TEXT")
    private String report;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
