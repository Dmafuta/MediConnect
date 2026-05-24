package com.mediconnect.lab.entity;

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
@Table(name = "lab_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class LabOrder extends AuditBase {

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

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_code")
    private String testCode;

    // ROUTINE, URGENT, STAT
    @Column(name = "priority", nullable = false)
    private String priority = "ROUTINE";

    // ORDERED, SAMPLE_COLLECTED, PROCESSING, RESULTED, CANCELLED
    @Column(name = "status", nullable = false)
    private String status = "ORDERED";

    @Column(name = "clinical_indication", columnDefinition = "TEXT")
    private String clinicalIndication;

    @Column(name = "result_value", columnDefinition = "TEXT")
    private String resultValue;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "result_range")
    private String resultRange;

    // NORMAL, ABNORMAL, CRITICAL
    @Column(name = "result_interpretation")
    private String resultInterpretation;

    @Column(name = "result_date")
    private LocalDateTime resultDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
