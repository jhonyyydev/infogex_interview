package com.gps.pruebaTecnica.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gps.pruebaTecnica.user.domain.Role;

public interface JpaRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
