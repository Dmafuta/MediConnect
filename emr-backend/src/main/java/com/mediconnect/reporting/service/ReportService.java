package com.mediconnect.reporting.service;

import com.mediconnect.reporting.dto.DashboardMetrics;
import com.mediconnect.lab.enums.LabOrderStatus;
import com.mediconnect.radiology.enums.ImagingOrderStatus;
import com.mediconnect.clinical.enums.ReferralStatus;
import com.mediconnect.security.repository.*;
import com.mediconnect.patient.repository.*;
import com.mediconnect.appointment.repository.*;
import com.mediconnect.clinical.repository.*;
import com.mediconnect.emergency.repository.*;
import com.mediconnect.lab.repository.*;
import com.mediconnect.radiology.repository.*;
import com.mediconnect.pharmacy.repository.*;
import com.mediconnect.nursing.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final VisitRepository visitRepository;
    private final AppointmentRepository appointmentRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final LabOrderRepository labOrderRepository;
    private final ImagingOrderRepository imagingOrderRepository;
    private final ReferralOrderRepository referralOrderRepository;
    private final PatientRepository patientRepository;

    public ReportService(VisitRepository visitRepository,
                          AppointmentRepository appointmentRepository,
                          DiagnosisRepository diagnosisRepository,
                          LabOrderRepository labOrderRepository,
                          ImagingOrderRepository imagingOrderRepository,
                          ReferralOrderRepository referralOrderRepository,
                          PatientRepository patientRepository) {
        this.visitRepository = visitRepository;
        this.appointmentRepository = appointmentRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.labOrderRepository = labOrderRepository;
        this.imagingOrderRepository = imagingOrderRepository;
        this.referralOrderRepository = referralOrderRepository;
        this.patientRepository = patientRepository;
    }

    public DashboardMetrics getDashboard() {
        LocalDate today = LocalDate.now();
        return DashboardMetrics.builder()
                .todayVisits(visitRepository.countByVisitDate(today))
                .todayAppointments(appointmentRepository.countByAppointmentDate(today))
                .pendingLabOrders(labOrderRepository.countByStatus(LabOrderStatus.ORDERED))
                .pendingImagingOrders(imagingOrderRepository.countByStatus(ImagingOrderStatus.ORDERED))
                .pendingReferrals(referralOrderRepository.countByStatus(ReferralStatus.PENDING))
                .totalActivePatients(patientRepository.countByIsActiveTrue())
                .build();
    }

    // Returns { "2026-05-01": 12, "2026-05-02": 8, ... }
    public Map<String, Long> getPatientFlow(LocalDate from, LocalDate to) {
        List<Object[]> rows = visitRepository.countByDateRange(from, to);
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return result;
    }

    // Returns { "BOOKED": 40, "COMPLETED": 30, "NO_SHOW": 5, "CANCELLED": 5 }
    public Map<String, Long> getAppointmentAdherence(LocalDate from, LocalDate to) {
        List<Object[]> rows = appointmentRepository.countByStatusInRange(from, to);
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((String) row[0], ((Number) row[1]).longValue());
        }
        return result;
    }

    // Returns { "Hypertension": 15, "Type 2 Diabetes": 12, ... }
    public Map<String, Long> getCommonDiagnoses(LocalDate from, LocalDate to, int limit) {
        List<Object[]> rows = diagnosisRepository.countByDescriptionInRange(from, to, limit);
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((String) row[0], ((Number) row[1]).longValue());
        }
        return result;
    }

    // Returns { "ORDERED": 10, "RESULTED": 45, "CANCELLED": 3 }
    public Map<String, Long> getLabOrderStats(LocalDate from, LocalDate to) {
        List<Object[]> rows = labOrderRepository.countByStatusInRange(from, to);
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((String) row[0], ((Number) row[1]).longValue());
        }
        return result;
    }
}
