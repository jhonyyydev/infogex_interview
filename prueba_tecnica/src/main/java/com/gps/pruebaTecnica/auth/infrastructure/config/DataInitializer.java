package com.gps.pruebaTecnica.auth.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.gps.pruebaTecnica.user.domain.Role;
import com.gps.pruebaTecnica.user.domain.User;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaRoleRepository;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaUserRepository;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    public CommandLineRunner initRoles(JpaRoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role("ADMIN"));
                roleRepository.save(new Role("INSURANCE_AGENT"));
                System.out.println("Roles initialized.");
            } else {
                System.out.println("Roles already exist.");

            }
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner initAdminUser(JpaUserRepository userRepository, JpaRoleRepository roleRepository) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
            if (adminRole == null) {
                adminRole = roleRepository.save(new Role("ADMIN"));
            }

            boolean userExists = userRepository.existsByRolesContaining(adminRole);
            if (!userExists) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword("$2a$12$KlRsIh6JKvngX3RLOIKICeaOFfAxXEcB/sFVx5fZylISiLGZOsO8e");
                adminUser.setEmail("admin@gerprosol.com");
                adminUser.setFirstName("Administrador");
                adminUser.getRoles().add(adminRole);
                userRepository.save(adminUser);
            }
        };
    }
}
