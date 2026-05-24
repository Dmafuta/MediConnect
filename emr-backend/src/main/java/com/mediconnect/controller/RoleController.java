package com.mediconnect.controller;

import com.mediconnect.entity.Role;
import com.mediconnect.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_roles') or hasRole('System Admin')")
    public List<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_role_details') or hasRole('System Admin')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_role') or hasRole('System Admin')")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_role') or hasRole('System Admin')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete_role') or hasRole('System Admin')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('assign_role_permissions') or hasRole('System Admin')")
    public ResponseEntity<Role> assignPermissionsToRole(@PathVariable Long id, @RequestBody Map<String, List<Long>> payload) {
        List<Long> permissionIds = payload.get("permissionIds");
        return ResponseEntity.ok(roleService.assignPermissionsToRole(id, permissionIds));
    }
}
