package com.mediconnect.clinical.controller;

import com.mediconnect.clinical.dto.ReferralOrderRequest;
import com.mediconnect.clinical.entity.ReferralOrder;
import com.mediconnect.clinical.service.ReferralOrderService;
import com.mediconnect.shared.dto.OrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits/{visitId}/orders/referrals")
public class ReferralOrderController {

    private final ReferralOrderService referralOrderService;

    public ReferralOrderController(ReferralOrderService referralOrderService) {
        this.referralOrderService = referralOrderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public List<ReferralOrder> getReferralOrders(@PathVariable Long visitId) {
        return referralOrderService.findByVisit(visitId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ReferralOrder createReferral(@PathVariable Long visitId,
                                         @Valid @RequestBody ReferralOrderRequest request) {
        return referralOrderService.create(visitId, request);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('doctors-notes-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<ReferralOrder> updateStatus(@PathVariable Long visitId,
                                                       @PathVariable Long orderId,
                                                       @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(referralOrderService.updateStatus(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> cancel(@PathVariable Long visitId, @PathVariable Long orderId) {
        referralOrderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
