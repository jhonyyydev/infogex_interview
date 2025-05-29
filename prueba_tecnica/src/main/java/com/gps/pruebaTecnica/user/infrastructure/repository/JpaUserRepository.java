package com.gps.pruebaTecnica.user.infrastructure.repository;

import com.gps.pruebaTecnica.user.domain.Role;
import com.gps.pruebaTecnica.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRolesContaining(Role role);

    boolean existsByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    Optional<User> findUserByUsername(String username);

}
