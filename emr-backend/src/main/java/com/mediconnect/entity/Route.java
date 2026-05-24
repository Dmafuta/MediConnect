package com.mediconnect.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rbac_route_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"childRoutes", "parentRoute", "permission"})
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    @EqualsAndHashCode.Include
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
    @JsonBackReference("route-children")
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

    @OneToMany(mappedBy = "parentRoute", cascade = CascadeType.ALL)
    @JsonManagedReference("route-children")
    private List<Route> childRoutes;
}
