package com.mediconnect.appointment.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(exclude = {"patient", "provider", "visit"})
public class Appointment extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // Nullable — walk-in patients may not be registered
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Walk-in patient fields (used when patient is null)
    @Column(name = "walkin_first_name")
    private String walkinFirstName;

    @Column(name = "walkin_last_name")
    private String walkinLastName;

    @Column(name = "walkin_contact_number")
    private String walkinContactNumber;

    @Column(name = "walkin_gender")
    private String walkinGender;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @Column(name = "department_name")
    private String departmentName;

    // ROUTINE, FOLLOW_UP, EMERGENCY, WALK_IN
    @Column(name = "appointment_type")
    private String appointmentType;

    // BOOKED, ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    @Column(name = "appointment_status", nullable = false)
    private String appointmentStatus = "BOOKED";

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "cancelled_on")
    private LocalDateTime cancelledOn;

    @Column(name = "cancelled_by")
    private String cancelledBy;

    @Column(name = "cancelled_remarks", columnDefinition = "TEXT")
    private String cancelledRemarks;

    @JsonIgnore
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Visit visit;
}
