package com.gps.pruebaTecnica.user.application;

import com.gps.pruebaTecnica.user.domain.Role;
import com.gps.pruebaTecnica.user.domain.User;
import com.gps.pruebaTecnica.user.dto.UserRequestDTO;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaRoleRepository;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final JpaUserRepository userRepository;
    private final JpaRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(JpaUserRepository userRepository, JpaRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()) ||
                userRepository.existsByUsername(dto.getUsername()) ||
                userRepository.existsByDocumentNumberAndDocumentType(dto.getDocumentNumber(), dto.getDocumentType())) {
            return Optional.empty();
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : dto.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDocumentType(dto.getDocumentType());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setRoles(roles);

        return Optional.of(userRepository.save(user));
    }



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id){
        return userRepository.findById(id);
    }


    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }



}
