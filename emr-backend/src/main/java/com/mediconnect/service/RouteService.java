package com.mediconnect.service;

import com.mediconnect.entity.Permission;
import com.mediconnect.entity.Route;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.PermissionRepository;
import com.mediconnect.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    public Page<Route> findAllRoutes(Pageable pageable) {
        return routeRepository.findAll(pageable);
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
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + route.getPermission().getId()));
            route.setPermission(permission);
        }
        if (route.getParentRoute() != null && route.getParentRoute().getId() != null) {
            Route parentRoute = routeRepository.findById(route.getParentRoute().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent route not found with id: " + route.getParentRoute().getId()));
            route.setParentRoute(parentRoute);
        }
        route.setIsActive(true);
        return routeRepository.save(route);
    }

    @Transactional
    public Route updateRoute(Long id, Route routeDetails) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));

        route.setUrlFullPath(routeDetails.getUrlFullPath());
        route.setDisplayName(routeDetails.getDisplayName());
        route.setDefaultShow(routeDetails.getDefaultShow());
        route.setRouterLink(routeDetails.getRouterLink());
        route.setIsActive(routeDetails.getIsActive());
        route.setIsSecondaryNavInDropdown(routeDetails.getIsSecondaryNavInDropdown());
        route.setCss(routeDetails.getCss());
        route.setDisplaySeq(routeDetails.getDisplaySeq());

        if (routeDetails.getPermission() != null && routeDetails.getPermission().getId() != null) {
            Permission permission = permissionRepository.findById(routeDetails.getPermission().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + routeDetails.getPermission().getId()));
            route.setPermission(permission);
        } else {
            route.setPermission(null);
        }

        if (routeDetails.getParentRoute() != null && routeDetails.getParentRoute().getId() != null) {
            Route parentRoute = routeRepository.findById(routeDetails.getParentRoute().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent route not found with id: " + routeDetails.getParentRoute().getId()));
            route.setParentRoute(parentRoute);
        } else {
            route.setParentRoute(null);
        }

        return routeRepository.save(route);
    }

    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        route.setIsActive(false);
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
