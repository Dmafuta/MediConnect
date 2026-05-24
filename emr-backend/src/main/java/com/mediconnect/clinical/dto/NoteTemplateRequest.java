package com.mediconnect.clinical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteTemplateRequest {

    @NotBlank
    private String templateName;

    private String visitType;
    private String noteType;
    private String subjectiveTemplate;
    private String objectiveTemplate;
    private String assessmentTemplate;
    private String planTemplate;
    private String contentTemplate;
}
