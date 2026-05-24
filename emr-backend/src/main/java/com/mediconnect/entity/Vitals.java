package com.mediconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "patient_vitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Vitals extends AuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @Column(name = "height")
    private Double height;

    @Column(name = "height_unit")
    private String heightUnit; // cm, in

    @Column(name = "weight")
    private Double weight;

    @Column(name = "weight_unit")
    private String weightUnit; // kg, lb

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "temperature_unit")
    private String temperatureUnit; // F, C

    @Column(name = "pulse")
    private Integer pulse;

    @Column(name = "bp_systolic")
    private Integer bpSystolic;

    @Column(name = "bp_diastolic")
    private Integer bpDiastolic;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @Column(name = "sp_o2")
    private Double spO2;

    @Column(name = "pain_scale")
    private Integer painScale; // 0–10

    @Column(name = "free_notes", columnDefinition = "TEXT")
    private String freeNotes;

    @Column(name = "vitals_taken_on")
    private LocalDateTime vitalsTakenOn;
}
