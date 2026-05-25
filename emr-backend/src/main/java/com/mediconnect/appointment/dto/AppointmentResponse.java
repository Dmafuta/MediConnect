package com.mediconnect.appointment.dto;

import com.mediconnect.appointment.entity.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private String walkinFirstName;
    private String walkinLastName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Long providerId;
    private String providerName;
    private String departmentName;
    private String appointmentType;
    private String appointmentStatus;
    private String reason;

    public static AppointmentResponse from(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setAppointmentDate(a.getAppointmentDate());
        r.setAppointmentTime(a.getAppointmentTime());
        r.setDepartmentName(a.getDepartmentName());
        r.setAppointmentType(a.getAppointmentType());
        r.setAppointmentStatus(a.getAppointmentStatus() != null ? a.getAppointmentStatus().name() : null);
        r.setReason(a.getReason());

        if (a.getPatient() != null) {
            r.setPatientId(a.getPatient().getId());
            r.setPatientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName());
        } else {
            r.setWalkinFirstName(a.getWalkinFirstName());
            r.setWalkinLastName(a.getWalkinLastName());
        }

        if (a.getProvider() != null) {
            r.setProviderId(a.getProvider().getId());
            r.setProviderName(a.getProvider().getUsername());
        }

        return r;
    }
}
