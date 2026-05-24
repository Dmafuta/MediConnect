package com.mediconnect.clinical.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "referral_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ReferralOrder extends AuditBase {

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
    @JoinColumn(name = "referred_by_id", nullable = false)
    private User referredBy;

    @Column(name = "referred_to_specialty", nullable = false)
    private String referredToSpecialty;

    @Column(name = "referred_to_provider")
    private String referredToProvider;

    // ROUTINE, URGENT
    @Column(name = "priority", nullable = false)
    private String priority = "ROUTINE";

    // PENDING, ACKNOWLEDGED, ACCEPTED, COMPLETED, CANCELLED
    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @Column(name = "reason_for_referral", columnDefinition = "TEXT")
    private String reasonForReferral;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
