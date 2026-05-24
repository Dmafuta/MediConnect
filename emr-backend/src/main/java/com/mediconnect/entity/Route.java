package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rbac_route_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long id;

    @Column(name = "url_full_path")
    private String urlFullPath;

    @Column(name = "display_name")
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_route_id")
    private Route parentRoute;

    @Column(name = "default_show")
    private Boolean defaultShow;

    @Column(name = "router_link")
    private String routerLink;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_secondary_nav_in_dropdown")
    private Boolean isSecondaryNavInDropdown;

    @Column(name = "css")
    private String css;

    @Column(name = "display_seq")
    private Integer displaySeq;

    // Self-referencing relationship for child routes
    @OneToMany(mappedBy = "parentRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> childRoutes;
}
