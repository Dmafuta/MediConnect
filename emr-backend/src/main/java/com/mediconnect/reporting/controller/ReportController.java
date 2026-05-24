package com.mediconnect.reporting.controller;

import com.mediconnect.reporting.dto.DashboardMetrics;
import com.mediconnect.reporting.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('reports-main-view') or hasRole('System Admin') or hasRole('Doctor')")
    public DashboardMetrics getDashboard() {
        return reportService.getDashboard();
    }

    @GetMapping("/patient-flow")
    @PreAuthorize("hasAuthority('reports-main-view') or hasRole('System Admin') or hasRole('Doctor')")
    public Map<String, Long> getPatientFlow(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getPatientFlow(from, to);
    }

    @GetMapping("/appointment-adherence")
    @PreAuthorize("hasAuthority('reports-main-view') or hasRole('System Admin') or hasRole('Doctor')")
    public Map<String, Long> getAppointmentAdherence(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getAppointmentAdherence(from, to);
    }

    @GetMapping("/common-diagnoses")
    @PreAuthorize("hasAuthority('reports-main-view') or hasRole('System Admin') or hasRole('Doctor')")
    public Map<String, Long> getCommonDiagnoses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit) {
        return reportService.getCommonDiagnoses(from, to, limit);
    }

    @GetMapping("/lab-stats")
    @PreAuthorize("hasAuthority('reports-main-view') or hasRole('System Admin') or hasRole('Doctor')")
    public Map<String, Long> getLabStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getLabOrderStats(from, to);
    }
}
