package com.mediconnect.security.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory token blacklist for logout support.
 * For production, replace the set with a Redis or DB-backed store so invalidations
 * survive restarts and work across multiple instances.
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void blacklist(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
