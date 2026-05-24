package com.mediconnect.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VitalsRequest {

    private Double height;
    private String heightUnit;
    private Double weight;
    private String weightUnit;
    private Double bmi;
    private Double temperature;
    private String temperatureUnit;
    private Integer pulse;
    private Integer bpSystolic;
    private Integer bpDiastolic;
    private Integer respiratoryRate;
    private Double spO2;
    private Integer painScale;
    private String freeNotes;
    private LocalDateTime vitalsTakenOn;
}
