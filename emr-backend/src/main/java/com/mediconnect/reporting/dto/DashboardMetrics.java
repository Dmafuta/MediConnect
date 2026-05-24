package com.mediconnect.reporting.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardMetrics {
    private long todayVisits;
    private long todayAppointments;
    private long pendingLabOrders;
    private long pendingImagingOrders;
    private long pendingReferrals;
    private long totalActivePatients;
}
