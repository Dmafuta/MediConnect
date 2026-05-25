package com.mediconnect.patient.dto;

import com.mediconnect.patient.entity.Patient;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
public class PatientResponse {

    private Long id;
    private String mrn;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private Integer age;
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
    private Boolean isActive;

    public static PatientResponse from(Patient p) {
        PatientResponse r = new PatientResponse();
        r.setId(p.getId());
        r.setMrn(p.getMrn());
        r.setSalutation(p.getSalutation());
        r.setFirstName(p.getFirstName());
        r.setMiddleName(p.getMiddleName());
        r.setLastName(p.getLastName());
        String mid = p.getMiddleName() != null ? " " + p.getMiddleName() : "";
        r.setFullName(p.getFirstName() + mid + " " + p.getLastName());
        r.setDateOfBirth(p.getDateOfBirth());
        if (p.getDateOfBirth() != null) {
            r.setAge(Period.between(p.getDateOfBirth(), LocalDate.now()).getYears());
        }
        r.setGender(p.getGender());
        r.setBloodGroup(p.getBloodGroup());
        r.setMaritalStatus(p.getMaritalStatus());
        r.setContactNumber(p.getContactNumber());
        r.setAlternatePhone(p.getAlternatePhone());
        r.setEmail(p.getEmail());
        r.setAddress(p.getAddress());
        r.setEmergencyContactName(p.getEmergencyContactName());
        r.setEmergencyContactRelation(p.getEmergencyContactRelation());
        r.setEmergencyContactNumber(p.getEmergencyContactNumber());
        r.setIsActive(p.getIsActive());
        return r;
    }
}
