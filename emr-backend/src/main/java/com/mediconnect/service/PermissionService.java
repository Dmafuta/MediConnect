package com.mediconnect.service;

import com.mediconnect.entity.Application;
import com.mediconnect.entity.Permission;
import com.mediconnect.repository.ApplicationRepository;
import com.mediconnect.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final ApplicationRepository applicationRepository;

    public PermissionService(PermissionRepository permissionRepository, ApplicationRepository applicationRepository) {
        this.permissionRepository = permissionRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    public Optional<Permission> findPermissionByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    @Transactional
    public Permission createPermission(Permission permission) {
        // Ensure the application exists
        Application application = applicationRepository.findById(permission.getApplication().getId())
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + permission.getApplication().getId()));
        permission.setApplication(application);

        permission.setIsActive(true);
        permission.setCreatedOn(LocalDateTime.now());
        // createdBy should be set from authenticated user context
        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission updatePermission(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        permission.setPermissionName(permissionDetails.getPermissionName());
        permission.setDescription(permissionDetails.getDescription());
        permission.setIsActive(permissionDetails.getIsActive());
        permission.setModifiedOn(LocalDateTime.now());
        // modifiedBy should be set from authenticated user context

        // Update application if provided
        if (permissionDetails.getApplication() != null && permissionDetails.getApplication().getId() != null) {
            Application application = applicationRepository.findById(permissionDetails.getApplication().getId())
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + permissionDetails.getApplication().getId()));
            permission.setApplication(application);
        }
        return permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        permission.setIsActive(false); // Soft delete
        permission.setModifiedOn(LocalDateTime.now());
        permissionRepository.save(permission);
    }
}
