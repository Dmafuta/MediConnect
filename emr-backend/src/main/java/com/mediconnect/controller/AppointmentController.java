package com.mediconnect.controller;

import com.mediconnect.dto.AppointmentRequest;
import com.mediconnect.dto.AppointmentStatusRequest;
import com.mediconnect.entity.Appointment;
import com.mediconnect.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('appointment-listappointment-view') or hasRole('System Admin')")
    public Page<Appointment> getAllAppointments(@PageableDefault(size = 15) Pageable pageable) {
        return appointmentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('appointment-view') or hasRole('System Admin')")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasAuthority('appointment-listappointment-view') or hasRole('System Admin')")
    public List<Appointment> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.findByDate(date);
    }

    @GetMapping("/by-provider")
    @PreAuthorize("hasAuthority('appointment-listappointment-view') or hasRole('System Admin')")
    public List<Appointment> getByProvider(
            @RequestParam Long providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.findByProviderAndDate(providerId, date);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('appointment-createappointment-view') or hasRole('System Admin') or hasRole('Receptionist')")
    public Appointment createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('appointment-visit-view') or hasRole('System Admin') or hasRole('Receptionist')")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id,
                                                     @Valid @RequestBody AppointmentStatusRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Receptionist')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
