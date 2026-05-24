package com.mediconnect.service;

import com.mediconnect.entity.Permission;
import com.mediconnect.entity.Route;
import com.mediconnect.repository.PermissionRepository;
import com.mediconnect.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PermissionRepository permissionRepository;

    public RouteService(RouteRepository routeRepository, PermissionRepository permissionRepository) {
        this.routeRepository = routeRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Route> findAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Optional<Route> findRouteByUrlFullPath(String urlFullPath) {
        return routeRepository.findByUrlFullPath(urlFullPath);
    }

    @Transactional
    public Route createRoute(Route route) {
        if (route.getPermission() != null && route.getPermission().getId() != null) {
            Permission permission = permissionRepository.findById(route.getPermission().getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + route.getPermission().getId()));
            route.setPermission(permission);
        }
        if (route.getParentRoute() != null && route.getParentRoute().getId() != null) {
            Route parentRoute = routeRepository.findById(route.getParentRoute().getId())
                    .orElseThrow(() -> new RuntimeException("Parent Route not found with id: " + route.getParentRoute().getId()));
            route.setParentRoute(parentRoute);
        }
        route.setIsActive(true);
        // createdBy, createdOn should be set from authenticated user context
        return routeRepository.save(route);
    }

    @Transactional
    public Route updateRoute(Long id, Route routeDetails) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));

        route.setUrlFullPath(routeDetails.getUrlFullPath());
        route.setDisplayName(routeDetails.getDisplayName());
        route.setDefaultShow(routeDetails.getDefaultShow());
        route.setRouterLink(routeDetails.getRouterLink());
        route.setIsActive(routeDetails.getIsActive());
        route.setIsSecondaryNavInDropdown(routeDetails.getIsSecondaryNavInDropdown());
        route.setCss(routeDetails.getCss());
        route.setDisplaySeq(routeDetails.getDisplaySeq());
        // modifiedBy, modifiedOn should be set from authenticated user context

        if (routeDetails.getPermission() != null && routeDetails.getPermission().getId() != null) {
            Permission permission = permissionRepository.findById(routeDetails.getPermission().getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + routeDetails.getPermission().getId()));
            route.setPermission(permission);
        } else {
            route.setPermission(null); // Disassociate permission if not provided
        }

        if (routeDetails.getParentRoute() != null && routeDetails.getParentRoute().getId() != null) {
            Route parentRoute = routeRepository.findById(routeDetails.getParentRoute().getId())
                    .orElseThrow(() -> new RuntimeException("Parent Route not found with id: " + routeDetails.getParentRoute().getId()));
            route.setParentRoute(parentRoute);
        } else {
            route.setParentRoute(null); // Disassociate parent route if not provided
        }

        return routeRepository.save(route);
    }

    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
        route.setIsActive(false); // Soft delete
        // modifiedBy, modifiedOn should be set from authenticated user context
        routeRepository.save(route);
    }

    public List<Route> findRoutesHierarchy() {
        List<Route> rootRoutes = routeRepository.findByParentRouteIsNullOrderByDisplaySeqAsc();
        rootRoutes.forEach(this::loadChildRoutes);
        return rootRoutes;
    }

    private void loadChildRoutes(Route route) {
        List<Route> childRoutes = routeRepository.findByParentRouteIdOrderByDisplaySeqAsc(route.getId());
        if (!childRoutes.isEmpty()) {
            route.setChildRoutes(childRoutes);
            childRoutes.forEach(this::loadChildRoutes);
        }
    }
}
