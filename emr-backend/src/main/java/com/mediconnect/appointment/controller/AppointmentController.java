package com.mediconnect.appointment.controller;

import com.mediconnect.appointment.dto.AppointmentRequest;
import com.mediconnect.appointment.dto.AppointmentResponse;
import com.mediconnect.appointment.dto.AppointmentStatusRequest;
import com.mediconnect.appointment.service.AppointmentService;
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
    public Page<AppointmentResponse> getAllAppointments(@PageableDefault(size = 15) Pageable pageable) {
        return appointmentService.findAll(pageable).map(AppointmentResponse::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('appointment-view') or hasRole('System Admin')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(AppointmentResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasAuthority('appointment-listappointment-view') or hasRole('System Admin')")
    public List<AppointmentResponse> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.findByDate(date).stream().map(AppointmentResponse::from).toList();
    }

    @GetMapping("/by-provider")
    @PreAuthorize("hasAuthority('appointment-listappointment-view') or hasRole('System Admin')")
    public List<AppointmentResponse> getByProvider(
            @RequestParam Long providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.findByProviderAndDate(providerId, date).stream().map(AppointmentResponse::from).toList();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('appointment-createappointment-view') or hasRole('System Admin') or hasRole('Receptionist')")
    public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return AppointmentResponse.from(appointmentService.create(request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('appointment-visit-view') or hasRole('System Admin') or hasRole('Receptionist')")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id,
                                                     @Valid @RequestBody AppointmentStatusRequest request) {
        return ResponseEntity.ok(AppointmentResponse.from(appointmentService.updateStatus(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Receptionist')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
