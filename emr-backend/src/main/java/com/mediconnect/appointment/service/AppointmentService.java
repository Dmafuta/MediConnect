package com.mediconnect.appointment.service;

import com.mediconnect.appointment.dto.AppointmentRequest;
import com.mediconnect.appointment.dto.AppointmentStatusRequest;
import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.security.entity.User;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.patient.repository.PatientRepository;
import com.mediconnect.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               PatientRepository patientRepository,
                               UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> findByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> findByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<Appointment> findByProviderAndDate(Long providerId, LocalDate date) {
        return appointmentRepository.findByProviderIdAndAppointmentDate(providerId, date);
    }

    @Transactional
    public Appointment create(AppointmentRequest request) {
        Appointment appointment = new Appointment();

        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
            appointment.setPatient(patient);
        } else {
            appointment.setWalkinFirstName(request.getWalkinFirstName());
            appointment.setWalkinLastName(request.getWalkinLastName());
            appointment.setWalkinContactNumber(request.getWalkinContactNumber());
            appointment.setWalkinGender(request.getWalkinGender());
        }

        User provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

        // Conflict check — only when a specific time slot is requested
        if (request.getAppointmentTime() != null) {
            boolean conflict = appointmentRepository
                    .existsByProviderIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNotIn(
                            request.getProviderId(),
                            request.getAppointmentDate(),
                            request.getAppointmentTime(),
                            List.of("CANCELLED", "NO_SHOW"));
            if (conflict) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Provider already has an appointment at " + request.getAppointmentDate()
                        + " " + request.getAppointmentTime() + ". Please choose a different time slot.");
            }
        }

        appointment.setProvider(provider);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDepartmentName(request.getDepartmentName());
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setReason(request.getReason());
        appointment.setAppointmentStatus("BOOKED");

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateStatus(Long id, AppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointment.setAppointmentStatus(request.getStatus());
        if ("CANCELLED".equals(request.getStatus())) {
            appointment.setCancelledRemarks(request.getCancelledRemarks());
            appointment.setCancelledOn(request.getCancelledOn());
            appointment.setCancelledBy(request.getCancelledBy());
        }
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}
