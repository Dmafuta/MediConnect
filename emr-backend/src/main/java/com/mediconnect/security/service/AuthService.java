package com.mediconnect.security.service;

import com.mediconnect.security.repository.UserRepository;
import com.mediconnect.shared.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    public String login(String username, String password) {
        if (loginAttemptService.isBlocked(username)) {
            log.warn("Login blocked for user {} — too many failed attempts", username);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Too many failed login attempts. Please try again in 15 minutes.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptService.loginSucceeded(username);
            log.info("Successful login for user {}", username);
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(username);
            log.warn("Failed login attempt for user {}", username);
            throw e;
        }
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return jwtUtil.generateToken(username, Boolean.TRUE.equals(user.getNeedsPasswordUpdate()));
    }
}
