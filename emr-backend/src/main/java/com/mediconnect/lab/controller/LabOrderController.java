package com.mediconnect.lab.controller;

import com.mediconnect.lab.dto.LabOrderRequest;
import com.mediconnect.lab.dto.LabResultRequest;
import com.mediconnect.lab.entity.LabOrder;
import com.mediconnect.lab.service.LabOrderService;
import com.mediconnect.shared.dto.OrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits/{visitId}/orders/lab")
public class LabOrderController {

    private final LabOrderService labOrderService;

    public LabOrderController(LabOrderService labOrderService) {
        this.labOrderService = labOrderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('lab-settings-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<LabOrder> getLabOrders(@PathVariable Long visitId) {
        return labOrderService.findByVisit(visitId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('lab-settings-view') or hasRole('System Admin') or hasRole('Doctor')")
    public LabOrder createLabOrder(@PathVariable Long visitId,
                                    @Valid @RequestBody LabOrderRequest request) {
        return labOrderService.create(visitId, request);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('lab-settings-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<LabOrder> updateStatus(@PathVariable Long visitId,
                                                  @PathVariable Long orderId,
                                                  @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(labOrderService.updateStatus(orderId, request));
    }

    @PutMapping("/{orderId}/result")
    @PreAuthorize("hasAuthority('lab-settings-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<LabOrder> recordResult(@PathVariable Long visitId,
                                                  @PathVariable Long orderId,
                                                  @Valid @RequestBody LabResultRequest request) {
        return ResponseEntity.ok(labOrderService.recordResult(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> cancel(@PathVariable Long visitId, @PathVariable Long orderId) {
        labOrderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
