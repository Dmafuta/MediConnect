package com.mediconnect.service;

import com.mediconnect.entity.Permission;
import com.mediconnect.entity.Role;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.PermissionRepository;
import com.mediconnect.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Transactional
    public Role createRole(Role role) {
        role.setIsActive(true);
        role.setCreatedOn(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        role.setRoleName(roleDetails.getRoleName());
        role.setRoleDescription(roleDetails.getRoleDescription());
        role.setRoleType(roleDetails.getRoleType());
        role.setIsSysAdmin(roleDetails.getIsSysAdmin());
        role.setIsActive(roleDetails.getIsActive());
        role.setRolePriority(roleDetails.getRolePriority());
        role.setModifiedOn(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setIsActive(false);
        role.setModifiedOn(LocalDateTime.now());
        roleRepository.save(role);
    }

    @Transactional
    public Role assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.setPermissions(permissions);
        role.setModifiedOn(LocalDateTime.now());
        return roleRepository.save(role);
    }
}
