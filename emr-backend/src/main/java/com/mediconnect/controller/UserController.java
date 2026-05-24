package com.mediconnect.controller;

import com.mediconnect.dto.UserChangePasswordRequest;
import com.mediconnect.dto.UserCreateRequest;
import com.mediconnect.entity.User;
import com.mediconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_users') or hasRole('System Admin')")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_user_details') or hasRole('System Admin')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create_user') or hasRole('System Admin')")
    public User createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_user') or hasRole('System Admin')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAuthority('update_user') or hasRole('System Admin')")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody UserChangePasswordRequest request) {
        userService.changePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete_user') or hasRole('System Admin')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('assign_user_roles') or hasRole('System Admin')")
    public ResponseEntity<User> assignRolesToUser(@PathVariable Long id, @RequestBody Map<String, List<Long>> payload) {
        List<Long> roleIds = payload.get("roleIds");
        return ResponseEntity.ok(userService.assignRolesToUser(id, roleIds));
    }
}
