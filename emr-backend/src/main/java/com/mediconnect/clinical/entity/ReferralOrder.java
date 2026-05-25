package com.mediconnect.clinical.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.clinical.enums.ReferralStatus;
import com.mediconnect.shared.enums.OrderPriority;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private OrderPriority priority = OrderPriority.ROUTINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status = ReferralStatus.PENDING;

    @Column(name = "reason_for_referral", columnDefinition = "TEXT")
    private String reasonForReferral;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
