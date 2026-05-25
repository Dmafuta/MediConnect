package com.mediconnect.clinical.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.clinical.enums.BillingStatus;
import com.mediconnect.clinical.enums.QueueStatus;
import com.mediconnect.clinical.enums.VisitStatus;
import com.mediconnect.clinical.enums.VisitType;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "patient_visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"patient", "provider", "appointment", "parentVisit", "vitals", "diagnoses"})
public class Visit extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "visit_code", unique = true)
    private String visitCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Nullable — walk-in visits have no appointment
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visit_time")
    private LocalTime visitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @Column(name = "department_name")
    private String departmentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type")
    private VisitType visitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus = VisitStatus.QUEUED;

    @Enumerated(EnumType.STRING)
    @Column(name = "queue_status")
    private QueueStatus queueStatus = QueueStatus.WAITING;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_status")
    private BillingStatus billingStatus = BillingStatus.PENDING;

    @Column(name = "is_triaged")
    private Boolean isTriaged = false;

    @Column(name = "is_visit_continued")
    private Boolean isVisitContinued = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_visit_id")
    private Visit parentVisit;

    @JsonIgnore
    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Vitals> vitals = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Diagnosis> diagnoses = new ArrayList<>();
}
