package com.mediconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "patient_problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PatientProblem extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "problem_description", nullable = false, columnDefinition = "TEXT")
    private String problemDescription;

    // ACTIVE, CHRONIC, RESOLVED
    @Column(name = "current_status")
    private String currentStatus = "ACTIVE";

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "onset_date")
    private LocalDate onsetDate;

    @Column(name = "resolved_date")
    private LocalDate resolvedDate;

    @Column(name = "is_resolved")
    private Boolean isResolved = false;

    @Column(name = "is_principal_problem")
    private Boolean isPrincipalProblem = false;
}
