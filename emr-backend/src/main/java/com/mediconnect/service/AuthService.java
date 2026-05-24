package com.mediconnect.service;

import com.mediconnect.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public AuthService(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                       JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    public String login(String username, String password) {
        if (loginAttemptService.isBlocked(username)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Too many failed login attempts. Please try again in 15 minutes.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptService.loginSucceeded(username);
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(username);
            throw e;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    // TODO: Implement password change/reset logic
}
