package com.gps.pruebaTecnica.user.infrastructure.controller;

import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import com.gps.pruebaTecnica.user.dto.RoleDTO;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final JpaRoleRepository roleRepository;

    public RoleController(JpaRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<List<RoleDTO>>> getRoles() {
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .toList();

        ApiResponseDTO<List<RoleDTO>> response = ApiResponseDTO.<List<RoleDTO>>builder()
                .success(true)
                .message("Roles")
                .data(roles)
                .build();

        return ResponseEntity.ok(response);
    }

}
