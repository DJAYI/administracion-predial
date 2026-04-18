package com.agmdesarrollos.geopix.security.persistance.repository;

import com.agmdesarrollos.geopix.security.persistance.entities.Role;
import com.agmdesarrollos.geopix.security.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String name);
}
