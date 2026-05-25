package com.mediconnect.patient.entity;

import com.mediconnect.shared.entity.AuditBase;
import com.mediconnect.patient.enums.AllergyType;
import com.mediconnect.patient.enums.AllergySeverity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "patient_allergies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Allergy extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "allergen_name", nullable = false)
    private String allergenName;

    @Enumerated(EnumType.STRING)
    @Column(name = "allergy_type")
    private AllergyType allergyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private AllergySeverity severity;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "reaction")
    private String reaction;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
