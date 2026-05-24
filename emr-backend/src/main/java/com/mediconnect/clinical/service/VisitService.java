package com.mediconnect.clinical.service;

import com.mediconnect.clinical.dto.VisitRequest;
import com.mediconnect.clinical.dto.VisitStatusRequest;
import com.mediconnect.appointment.entity.Appointment;
import com.mediconnect.patient.entity.Patient;
import com.mediconnect.security.entity.User;
import com.mediconnect.clinical.entity.Visit;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import com.mediconnect.appointment.repository.AppointmentRepository;
import com.mediconnect.patient.repository.PatientRepository;
import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.clinical.repository.VisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public VisitService(VisitRepository visitRepository, PatientRepository patientRepository,
                        UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Page<Visit> findAll(Pageable pageable) {
        return visitRepository.findAll(pageable);
    }

    public Optional<Visit> findById(Long id) {
        return visitRepository.findById(id);
    }

    public List<Visit> findByPatient(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    public List<Visit> findTodaysQueue() {
        return visitRepository.findByVisitDateAndVisitStatus(LocalDate.now(), "QUEUED");
    }

    @Transactional
    public Visit create(VisitRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        User provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setProvider(provider);
        visit.setVisitDate(request.getVisitDate());
        visit.setVisitTime(request.getVisitTime() != null ? request.getVisitTime() : LocalTime.now());
        visit.setDepartmentName(request.getDepartmentName());
        visit.setVisitType(request.getVisitType() != null ? request.getVisitType() : "OPD");
        visit.setVisitStatus("QUEUED");
        visit.setQueueStatus("WAITING");
        visit.setBillingStatus("PENDING");

        if (request.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));
            visit.setAppointment(appointment);
            // Mark appointment as arrived
            appointment.setAppointmentStatus("ARRIVED");
            appointmentRepository.save(appointment);
        }

        if (request.getParentVisitId() != null) {
            Visit parent = visitRepository.findById(request.getParentVisitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent visit not found with id: " + request.getParentVisitId()));
            visit.setParentVisit(parent);
            visit.setIsVisitContinued(true);
        }

        visit = visitRepository.save(visit);
        visit.setVisitCode("V-" + String.format("%06d", visit.getId()));
        return visitRepository.save(visit);
    }

    @Transactional
    public Visit updateStatus(Long id, VisitStatusRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + id));
        visit.setVisitStatus(request.getVisitStatus());
        if (request.getQueueStatus() != null) visit.setQueueStatus(request.getQueueStatus());
        if (request.getBillingStatus() != null) visit.setBillingStatus(request.getBillingStatus());
        if (request.getIsTriaged() != null) visit.setIsTriaged(request.getIsTriaged());
        return visitRepository.save(visit);
    }

    @Transactional
    public void delete(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visit not found with id: " + id);
        }
        visitRepository.deleteById(id);
    }
}
