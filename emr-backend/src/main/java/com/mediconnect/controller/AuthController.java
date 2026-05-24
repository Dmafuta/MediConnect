package com.mediconnect.controller;

import com.mediconnect.dto.MeResponse;
import com.mediconnect.entity.Route;
import com.mediconnect.entity.User;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.service.AuthService;
import com.mediconnect.service.AuthorizationService;
import com.mediconnect.service.TokenBlacklistService;
import com.mediconnect.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService,
                          UserService userService, AuthorizationService authorizationService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        String token = authService.login(authRequest.getUsername(), authRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        List<String> roleNames = user.getRoles().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .map(r -> r.getRoleName())
                .collect(Collectors.toList());

        Set<String> permissions = authorizationService.getUserPermissions(user.getId());
        List<Route> accessibleRoutes = authorizationService.getUserAccessibleRoutes(user.getId());

        return ResponseEntity.ok(new MeResponse(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getIsActive(), user.getNeedsPasswordUpdate(),
                roleNames, permissions, accessibleRoutes));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklistService.blacklist(authHeader.substring(7));
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    static class AuthRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
