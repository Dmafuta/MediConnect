package com.mediconnect.security.repository;

import com.mediconnect.security.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByUrlFullPath(String urlFullPath);
    List<Route> findByParentRouteIsNullOrderByDisplaySeqAsc();
    List<Route> findByParentRouteIdOrderByDisplaySeqAsc(Long parentRouteId);
}
