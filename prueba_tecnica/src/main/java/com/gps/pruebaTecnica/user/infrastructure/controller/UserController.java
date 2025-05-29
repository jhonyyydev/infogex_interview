package com.gps.pruebaTecnica.user.infrastructure.controller;

import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import com.gps.pruebaTecnica.user.application.UserService;
import com.gps.pruebaTecnica.user.domain.User;
import com.gps.pruebaTecnica.user.dto.UserRequestDTO;
import com.gps.pruebaTecnica.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAll() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers()
                    .stream()
                    .map(UserResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponseDTO.<List<UserResponseDTO>>builder()
                    .success(true)
                    .message("Listado de usuarios obtenido correctamente")
                    .data(users)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<UserResponseDTO>>builder()
                    .success(false)
                    .message("Error al obtener el listado de usuarios")
                    .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> create(@RequestBody @Valid UserRequestDTO dto) {
        try {
            Optional<User> userOpt = userService.createUser(dto);
            if (userOpt.isPresent()) {
                System.out.println(dto.getEmail()+" Credenciales "+ dto.getUsername()+ dto.getPassword());
            }
            return userOpt.map(user -> ResponseEntity.ok(ApiResponseDTO.<UserResponseDTO>builder()
                    .success(true)
                    .message("Usuario creado exitosamente")
                    .data(new UserResponseDTO(user))
                    .build())).orElseGet(() -> ResponseEntity.badRequest().body(ApiResponseDTO.<UserResponseDTO>builder()
                    .success(false)
                    .message("Error al crear el usuario. Verifica que el correo y documento no estén repetidos.")
                    .build()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.<UserResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<UserResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
