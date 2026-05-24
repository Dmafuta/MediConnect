package com.mediconnect.service;

import com.mediconnect.entity.User;
import com.mediconnect.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet; // Added import
import java.util.Set;     // Added import
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert roles and permissions to Spring Security GrantedAuthorities
        // For simplicity, we'll add both role names and permission names as authorities
        // In a real application, you might want to be more granular with permissions
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.getIsActive(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                user.getRoles().stream()
                        .flatMap(role -> {
                            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
                            role.getPermissions().forEach(permission ->
                                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()))
                            );
                            return authorities.stream();
                        })
                        .collect(Collectors.toSet())
        );
    }
}