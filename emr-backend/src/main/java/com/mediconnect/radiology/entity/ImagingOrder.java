package com.mediconnect.radiology.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.radiology.enums.ImagingOrderStatus;
import com.mediconnect.radiology.enums.StudyType;
import com.mediconnect.shared.enums.OrderPriority;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false)
    private StudyType studyType;

    @Column(name = "body_part")
    private String bodyPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private OrderPriority priority = OrderPriority.ROUTINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImagingOrderStatus status = ImagingOrderStatus.ORDERED;

    @Column(name = "clinical_indication", columnDefinition = "TEXT")
    private String clinicalIndication;

    @Column(name = "report", columnDefinition = "TEXT")
    private String report;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
