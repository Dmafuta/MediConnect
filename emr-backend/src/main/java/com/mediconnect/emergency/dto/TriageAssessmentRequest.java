package com.mediconnect.emergency.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TriageAssessmentRequest {

    @NotBlank
    private String chiefComplaint;

    private String historyOfPresentIllness;

    @Min(1) @Max(5)
    private Integer esiLevel;

    private String onsetDuration;
    private String onsetCharacter;
    private String generalAppearance;
    private String mentalStatus;
    private String disposition;
    private String additionalNotes;
}
