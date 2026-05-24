package com.mediconnect.radiology.controller;

import com.mediconnect.radiology.dto.ImagingOrderRequest;
import com.mediconnect.radiology.entity.ImagingOrder;
import com.mediconnect.radiology.service.ImagingOrderService;
import com.mediconnect.shared.dto.OrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits/{visitId}/orders/imaging")
public class ImagingOrderController {

    private final ImagingOrderService imagingOrderService;

    public ImagingOrderController(ImagingOrderService imagingOrderService) {
        this.imagingOrderService = imagingOrderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('reports-radiologymain-view') or hasRole('System Admin') or hasRole('Doctor') or hasRole('Nurse')")
    public List<ImagingOrder> getImagingOrders(@PathVariable Long visitId) {
        return imagingOrderService.findByVisit(visitId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('reports-radiologymain-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ImagingOrder createImagingOrder(@PathVariable Long visitId,
                                            @Valid @RequestBody ImagingOrderRequest request) {
        return imagingOrderService.create(visitId, request);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('reports-radiologymain-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<ImagingOrder> updateStatus(@PathVariable Long visitId,
                                                      @PathVariable Long orderId,
                                                      @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(imagingOrderService.updateStatus(orderId, request));
    }

    @PutMapping("/{orderId}/report")
    @PreAuthorize("hasAuthority('reports-radiologymain-view') or hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<ImagingOrder> recordReport(@PathVariable Long visitId,
                                                      @PathVariable Long orderId,
                                                      @RequestBody String report) {
        return ResponseEntity.ok(imagingOrderService.recordReport(orderId, report));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('System Admin') or hasRole('Doctor')")
    public ResponseEntity<Void> cancel(@PathVariable Long visitId, @PathVariable Long orderId) {
        imagingOrderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
