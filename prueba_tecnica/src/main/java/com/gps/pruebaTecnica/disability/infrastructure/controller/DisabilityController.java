package com.gps.pruebaTecnica.disability.infrastructure.controller;

import com.gps.pruebaTecnica.disability.application.DisabilityService;
import com.gps.pruebaTecnica.disability.domain.Disability;
import com.gps.pruebaTecnica.disability.domain.DisabilityStatus;
import com.gps.pruebaTecnica.disability.domain.StatusHistory;
import com.gps.pruebaTecnica.disability.dto.DisabilityRequestDTO;
import com.gps.pruebaTecnica.disability.dto.DisabilityResponseDTO;
import com.gps.pruebaTecnica.disability.dto.StatusUpdateRequestDTO;
import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import com.gps.pruebaTecnica.user.domain.User;
import com.gps.pruebaTecnica.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disability")
@Tag(name = "Disability", description = "API para gestión de incapacidades")
public class DisabilityController {

    private final DisabilityService disabilityService;
    private final UserService userService;

    public DisabilityController(DisabilityService disabilityService, UserService userService) {
        this.disabilityService = disabilityService;
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear una nueva incapacidad", description = "Crea una nueva incapacidad y retorna el número de radicado")
    public ResponseEntity<ApiResponseDTO<DisabilityResponseDTO>> createDisability(@Valid @RequestBody DisabilityRequestDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                        .success(false)
                        .message("Usuario no encontrado")
                        .build());
            }

            Disability disability = disabilityService.createDisability(dto, userOpt.get());

            return ResponseEntity.ok(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(true)
                    .message("Incapacidad creada exitosamente. Número de radicado: " + disability.getFilingNumber())
                    .data(new DisabilityResponseDTO(disability))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(false)
                    .message("Error al crear la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar incapacidades del usuario", description = "Obtiene todas las incapacidades creadas por el usuario autenticado")
    public ResponseEntity<ApiResponseDTO<List<DisabilityResponseDTO>>> getMyDisabilities() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<List<DisabilityResponseDTO>>builder()
                        .success(false)
                        .message("Usuario no encontrado")
                        .build());
            }

            List<DisabilityResponseDTO> disabilities = disabilityService.getDisabilitiesByUser(userOpt.get())
                    .stream()
                    .map(DisabilityResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponseDTO.<List<DisabilityResponseDTO>>builder()
                    .success(true)
                    .message("Listado de incapacidades")
                    .data(disabilities)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<DisabilityResponseDTO>>builder()
                    .success(false)
                    .message("Error al obtener las incapacidades: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener incapacidad por ID", description = "Obtiene una incapacidad por su ID")
    public ResponseEntity<ApiResponseDTO<DisabilityResponseDTO>> getDisabilityById(@PathVariable UUID id) {
        try {
            Optional<Disability> disabilityOpt = disabilityService.getDisabilityById(id);

            if (disabilityOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(true)
                    .message("Incapacidad encontrada")
                    .data(new DisabilityResponseDTO(disabilityOpt.get()))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(false)
                    .message("Error al obtener la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/filing/{filingNumber}")
    @Operation(summary = "Obtener incapacidad por número de radicado", description = "Obtiene una incapacidad por su número de radicado")
    public ResponseEntity<ApiResponseDTO<DisabilityResponseDTO>> getDisabilityByFilingNumber(@PathVariable String filingNumber) {
        try {
            Optional<Disability> disabilityOpt = disabilityService.getDisabilityByFilingNumber(filingNumber);

            if (disabilityOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(true)
                    .message("Incapacidad encontrada")
                    .data(new DisabilityResponseDTO(disabilityOpt.get()))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(false)
                    .message("Error al obtener la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar incapacidad", description = "Actualiza los datos de una incapacidad existente")
    public ResponseEntity<ApiResponseDTO<DisabilityResponseDTO>> updateDisability(
            @PathVariable UUID id,
            @Valid @RequestBody DisabilityRequestDTO dto) {
        try {
            Optional<Disability> updatedDisabilityOpt = disabilityService.updateDisability(id, dto);

            if (updatedDisabilityOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                        .success(false)
                        .message("No se pudo actualizar la incapacidad. Verifica que exista y esté en estado PENDIENTE")
                        .build());
            }

            return ResponseEntity.ok(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(true)
                    .message("Incapacidad actualizada exitosamente")
                    .data(new DisabilityResponseDTO(updatedDisabilityOpt.get()))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(false)
                    .message("Error al actualizar la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de incapacidad", description = "Actualiza el estado de una incapacidad existente")
    public ResponseEntity<ApiResponseDTO<DisabilityResponseDTO>> updateDisabilityStatus(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequestDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                        .success(false)
                        .message("Usuario no encontrado")
                        .build());
            }

            Optional<Disability> updatedDisabilityOpt = disabilityService.updateStatus(id, dto, userOpt.get());

            if (updatedDisabilityOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                        .success(false)
                        .message("No se pudo actualizar el estado de la incapacidad")
                        .build());
            }

            return ResponseEntity.ok(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(true)
                    .message("Estado de incapacidad actualizado exitosamente")
                    .data(new DisabilityResponseDTO(updatedDisabilityOpt.get()))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<DisabilityResponseDTO>builder()
                    .success(false)
                    .message("Error al actualizar el estado de la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar incapacidad", description = "Elimina una incapacidad existente si está en estado PENDIENTE")
    public ResponseEntity<ApiResponseDTO<Void>> deleteDisability(@PathVariable UUID id) {
        try {
            boolean deleted = disabilityService.deleteDisability(id);

            if (!deleted) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<Void>builder()
                        .success(false)
                        .message("No se pudo eliminar la incapacidad. Verifica que exista y esté en estado PENDIENTE")
                        .build());
            }

            return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                    .success(true)
                    .message("Incapacidad eliminada exitosamente")
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<Void>builder()
                    .success(false)
                    .message("Error al eliminar la incapacidad: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Obtener historial de estados", description = "Obtiene el historial de cambios de estado de una incapacidad")
    public ResponseEntity<ApiResponseDTO<List<StatusHistory>>> getStatusHistory(@PathVariable UUID id) {
        try {
            List<StatusHistory> history = disabilityService.getStatusHistory(id);

            return ResponseEntity.ok(ApiResponseDTO.<List<StatusHistory>>builder()
                    .success(true)
                    .message("Historial de estados")
                    .data(history)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<StatusHistory>>builder()
                    .success(false)
                    .message("Error al obtener el historial de estados: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar incapacidades por estado y rango de fechas",
            description = "Obtiene las incapacidades que coinciden con el estado y rango de fechas especificados")
    public ResponseEntity<ApiResponseDTO<List<DisabilityResponseDTO>>> filterDisabilities(
            @RequestParam DisabilityStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<DisabilityResponseDTO> disabilities = disabilityService
                    .getDisabilitiesByStatusAndDateRange(status, startDate, endDate)
                    .stream()
                    .map(DisabilityResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponseDTO.<List<DisabilityResponseDTO>>builder()
                    .success(true)
                    .message("Incapacidades filtradas")
                    .data(disabilities)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<DisabilityResponseDTO>>builder()
                    .success(false)
                    .message("Error al filtrar las incapacidades: " + e.getMessage())
                    .build());
        }
    }
}
