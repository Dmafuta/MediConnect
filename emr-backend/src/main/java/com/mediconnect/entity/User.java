package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rbac_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId; // Foreign Key to Employee entity (not yet created)

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String passwordHash; // Stores hashed password

    @Column(name = "email")
    private String email;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "needs_password_update")
    private Boolean needsPasswordUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landing_page_route_id")
    private Route landingPageRoute;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rbac_map_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
