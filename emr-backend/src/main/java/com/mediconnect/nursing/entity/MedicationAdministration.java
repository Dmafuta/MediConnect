package com.mediconnect.nursing.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.security.entity.User;
import com.mediconnect.pharmacy.entity.MedicationPrescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medication_administrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class MedicationAdministration extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    // Nullable — ad-hoc doses not tied to a written prescription
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private MedicationPrescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administered_by_id", nullable = false)
    private User administeredBy;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "dose", nullable = false)
    private String dose;

    @Column(name = "route")
    private String route;

    @Column(name = "administered_at", nullable = false)
    private LocalDateTime administeredAt;

    // ADMINISTERED, HELD, REFUSED, MISSED
    @Column(name = "status", nullable = false)
    private String status = "ADMINISTERED";

    @Column(name = "hold_reason")
    private String holdReason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
