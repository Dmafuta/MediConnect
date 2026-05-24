package com.mediconnect.patient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientUpdateRequest {

    private String salutation;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    private String bloodGroup;
    private String maritalStatus;
    private String contactNumber;
    private String alternatePhone;
    private String email;
    private String address;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactNumber;
}
