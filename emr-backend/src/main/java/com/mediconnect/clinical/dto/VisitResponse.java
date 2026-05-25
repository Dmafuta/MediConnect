package com.mediconnect.clinical.dto;

import com.mediconnect.clinical.entity.Visit;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisitResponse {

    private Long id;
    private String visitCode;
    private Long patientId;
    private String patientName;
    private String patientMrn;
    private Long providerId;
    private String providerName;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String departmentName;
    private String visitType;
    private String visitStatus;
    private String queueStatus;
    private String billingStatus;
    private Boolean isTriaged;
    private Boolean isVisitContinued;
    private Long appointmentId;
    private Long parentVisitId;

    public static VisitResponse from(Visit v) {
        VisitResponse r = new VisitResponse();
        r.setId(v.getId());
        r.setVisitCode(v.getVisitCode());
        r.setVisitDate(v.getVisitDate());
        r.setVisitTime(v.getVisitTime());
        r.setDepartmentName(v.getDepartmentName());
        r.setVisitType(v.getVisitType() != null ? v.getVisitType().name() : null);
        r.setVisitStatus(v.getVisitStatus() != null ? v.getVisitStatus().name() : null);
        r.setQueueStatus(v.getQueueStatus() != null ? v.getQueueStatus().name() : null);
        r.setBillingStatus(v.getBillingStatus() != null ? v.getBillingStatus().name() : null);
        r.setIsTriaged(v.getIsTriaged());
        r.setIsVisitContinued(v.getIsVisitContinued());

        if (v.getPatient() != null) {
            r.setPatientId(v.getPatient().getId());
            r.setPatientName(v.getPatient().getFirstName() + " " + v.getPatient().getLastName());
            r.setPatientMrn(v.getPatient().getMrn());
        }

        if (v.getProvider() != null) {
            r.setProviderId(v.getProvider().getId());
            r.setProviderName(v.getProvider().getUsername());
        }

        if (v.getAppointment() != null) {
            r.setAppointmentId(v.getAppointment().getId());
        }

        if (v.getParentVisit() != null) {
            r.setParentVisitId(v.getParentVisit().getId());
        }

        return r;
    }
}
