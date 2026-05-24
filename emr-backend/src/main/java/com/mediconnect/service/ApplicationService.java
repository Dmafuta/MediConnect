package com.mediconnect.service;

import com.mediconnect.entity.Application;
import com.mediconnect.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Optional<Application> findApplicationByApplicationCode(String applicationCode) {
        return applicationRepository.findByApplicationCode(applicationCode);
    }

    @Transactional
    public Application createApplication(Application application) {
        application.setIsActive(true);
        application.setCreatedOn(LocalDateTime.now());
        // createdBy should be set from authenticated user context
        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateApplication(Long id, Application applicationDetails) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        application.setApplicationCode(applicationDetails.getApplicationCode());
        application.setApplicationName(applicationDetails.getApplicationName());
        application.setDescription(applicationDetails.getDescription());
        application.setIsActive(applicationDetails.getIsActive());
        application.setModifiedOn(LocalDateTime.now());
        // modifiedBy should be set from authenticated user context
        return applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        application.setIsActive(false); // Soft delete
        application.setModifiedOn(LocalDateTime.now());
        applicationRepository.save(application);
    }
}
