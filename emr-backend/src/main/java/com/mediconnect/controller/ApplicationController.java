package com.mediconnect.controller;

import com.mediconnect.entity.Application;
import com.mediconnect.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_applications') or hasRole('System Admin')")
    public Page<Application> getAllApplications(@PageableDefault(size = 15) Pageable pageable) {
        return applicationService.findAllApplications(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_application_details') or hasRole('System Admin')")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.findApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_application') or hasRole('System Admin')")
    public Application createApplication(@RequestBody Application application) {
        return applicationService.createApplication(application);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_application') or hasRole('System Admin')")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        return ResponseEntity.ok(applicationService.updateApplication(id, applicationDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete_application') or hasRole('System Admin')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
