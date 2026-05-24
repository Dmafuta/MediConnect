package com.mediconnect.security.controller;

import com.mediconnect.security.entity.Route;
import com.mediconnect.security.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_routes') or hasRole('System Admin')")
    public Page<Route> getAllRoutes(@PageableDefault(size = 15) Pageable pageable) {
        return routeService.findAllRoutes(pageable);
    }

    @GetMapping("/hierarchy")
    @PreAuthorize("hasAuthority('view_routes_hierarchy') or hasRole('System Admin')")
    public List<Route> getRoutesHierarchy() {
        return routeService.findRoutesHierarchy();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_route_details') or hasRole('System Admin')")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return routeService.findRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_route') or hasRole('System Admin')")
    public Route createRoute(@RequestBody Route route) {
        return routeService.createRoute(route);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_route') or hasRole('System Admin')")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route routeDetails) {
        return ResponseEntity.ok(routeService.updateRoute(id, routeDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete_route') or hasRole('System Admin')")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
