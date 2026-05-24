package com.mediconnect.controller;

import com.mediconnect.entity.Route;
import com.mediconnect.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/users/{userId}/permissions")
    @PreAuthorize("hasAuthority('view_user_permissions') or hasRole('System Admin')")
    public ResponseEntity<Set<String>> getUserPermissions(@PathVariable Long userId) {
        Set<String> permissions = authorizationService.getUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/users/{userId}/routes")
    @PreAuthorize("hasAuthority('view_user_routes') or hasRole('System Admin')")
    public ResponseEntity<List<Route>> getUserAccessibleRoutes(@PathVariable Long userId) {
        List<Route> routes = authorizationService.getUserAccessibleRoutes(userId);
        return ResponseEntity.ok(routes);
    }
}
