package com.mediconnect.pharmacy.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medication_prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class MedicationPrescription extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescribed_by_id", nullable = false)
    private User prescribedBy;

    // Optional — prescription may be written outside a formal visit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    // ORAL, IV, IM, TOPICAL, SUBLINGUAL, INHALATION, OTHER
    @Column(name = "route")
    private String route;

    @Column(name = "dose", nullable = false)
    private String dose;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration")
    private Integer duration;

    // DAYS, WEEKS, MONTHS
    @Column(name = "duration_type")
    private String durationType;

    @Column(name = "refill")
    private Integer refill;

    // TABLET, CAPSULE, INJECTION, SYRUP, CREAM, OTHER
    @Column(name = "type_of_medication")
    private String typeOfMedication;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
