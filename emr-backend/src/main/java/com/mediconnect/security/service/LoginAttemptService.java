package com.mediconnect.security.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks failed login attempts per username and blocks access after MAX_ATTEMPTS
 * failures within the block window.
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 15;

    private record AttemptInfo(int count, LocalDateTime lastAttempt) {
        AttemptInfo increment() {
            return new AttemptInfo(count + 1, LocalDateTime.now());
        }
    }

    private final ConcurrentHashMap<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public void loginFailed(String username) {
        attempts.merge(username, new AttemptInfo(1, LocalDateTime.now()), (existing, __) -> existing.increment());
    }

    public boolean isBlocked(String username) {
        AttemptInfo info = attempts.get(username);
        if (info == null || info.count() < MAX_ATTEMPTS) return false;
        if (info.lastAttempt().plusMinutes(BLOCK_DURATION_MINUTES).isAfter(LocalDateTime.now())) return true;
        attempts.remove(username);
        return false;
    }
}
