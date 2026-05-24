package com.mediconnect.service;

import com.mediconnect.entity.Route;
import com.mediconnect.entity.User;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final RouteService routeService;

    public AuthorizationService(UserRepository userRepository, RouteService routeService) {
        this.userRepository = userRepository;
        this.routeService = routeService;
    }

    @Transactional(readOnly = true)
    public Set<String> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(role -> {
            if (Boolean.TRUE.equals(role.getIsActive())) {
                role.getPermissions().forEach(permission -> {
                    if (Boolean.TRUE.equals(permission.getIsActive())) {
                        permissions.add(permission.getPermissionName());
                    }
                });
            }
        });
        return permissions;
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(Long userId, String permissionName) {
        return getUserPermissions(userId).contains(permissionName);
    }

    @Transactional(readOnly = true)
    public List<Route> getUserAccessibleRoutes(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<String> userPermissionNames = getUserPermissions(userId);

        List<Route> allRoutes = routeService.findAllRoutes().stream()
                .filter(route -> Boolean.TRUE.equals(route.getIsActive()))
                .collect(Collectors.toList());

        List<Route> accessibleRoutes = allRoutes.stream()
                .filter(route -> route.getPermission() == null
                        || userPermissionNames.contains(route.getPermission().getPermissionName()))
                .collect(Collectors.toList());

        List<Route> rootAccessibleRoutes = accessibleRoutes.stream()
                .filter(route -> route.getParentRoute() == null)
                .collect(Collectors.toList());

        rootAccessibleRoutes.forEach(rootRoute -> buildAccessibleRouteHierarchy(rootRoute, accessibleRoutes));

        return rootAccessibleRoutes;
    }

    private void buildAccessibleRouteHierarchy(Route currentRoute, List<Route> allAccessibleRoutes) {
        List<Route> childRoutes = allAccessibleRoutes.stream()
                .filter(route -> route.getParentRoute() != null
                        && route.getParentRoute().getId().equals(currentRoute.getId()))
                .collect(Collectors.toList());

        if (!childRoutes.isEmpty()) {
            currentRoute.setChildRoutes(childRoutes);
            childRoutes.forEach(childRoute -> buildAccessibleRouteHierarchy(childRoute, allAccessibleRoutes));
        }
    }
}
