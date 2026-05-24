package com.mediconnect.security.controller;

import com.mediconnect.security.entity.Permission;
import com.mediconnect.security.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_permissions') or hasRole('System Admin')")
    public Page<Permission> getAllPermissions(@PageableDefault(size = 15) Pageable pageable) {
        return permissionService.findAllPermissions(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_permission_details') or hasRole('System Admin')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return permissionService.findPermissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_permission') or hasRole('System Admin')")
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_permission') or hasRole('System Admin')")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permissionDetails) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permissionDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete_permission') or hasRole('System Admin')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
