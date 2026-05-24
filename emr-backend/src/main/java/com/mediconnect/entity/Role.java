package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rbac_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"permissions", "application", "defaultRoute"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "role_type")
    private String roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    @Column(name = "is_sys_admin", nullable = false)
    private Boolean isSysAdmin;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "role_priority")
    private Integer rolePriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_route_id")
    private Route defaultRoute;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rbac_map_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();
}
