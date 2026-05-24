package com.mediconnect.security.dto;

import com.mediconnect.security.entity.Route;

import java.util.List;
import java.util.Set;

public class MeResponse {

    private Long id;
    private String username;
    private String email;
    private Boolean isActive;
    private Boolean needsPasswordUpdate;
    private List<String> roles;
    private Set<String> permissions;
    private List<Route> accessibleRoutes;

    public MeResponse(Long id, String username, String email, Boolean isActive, Boolean needsPasswordUpdate,
                      List<String> roles, Set<String> permissions, List<Route> accessibleRoutes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.needsPasswordUpdate = needsPasswordUpdate;
        this.roles = roles;
        this.permissions = permissions;
        this.accessibleRoutes = accessibleRoutes;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Boolean getIsActive() { return isActive; }
    public Boolean getNeedsPasswordUpdate() { return needsPasswordUpdate; }
    public List<String> getRoles() { return roles; }
    public Set<String> getPermissions() { return permissions; }
    public List<Route> getAccessibleRoutes() { return accessibleRoutes; }
}
