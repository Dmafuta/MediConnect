package com.mediconnect.clinical.dto;

import lombok.Data;

@Data
public class ClinicalNoteRequest {

    // SOAP, PROGRESS, DISCHARGE, REFERRAL, OTHER
    private String noteType;

    // SOAP fields
    private String subjective;
    private String objective;
    private String assessment;
    private String plan;

    // Used for non-SOAP types
    private String noteContent;
}
