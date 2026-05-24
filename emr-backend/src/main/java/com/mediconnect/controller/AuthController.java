package com.mediconnect.controller;

import com.mediconnect.service.AuthService;
import com.mediconnect.service.TokenBlacklistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        String token = authService.login(authRequest.getUsername(), authRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
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
