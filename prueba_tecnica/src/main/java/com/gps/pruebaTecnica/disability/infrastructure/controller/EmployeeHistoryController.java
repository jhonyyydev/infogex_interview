package com.gps.pruebaTecnica.disability.infrastructure.controller;

import com.gps.pruebaTecnica.disability.application.EmployeeHistoryService;
import com.gps.pruebaTecnica.disability.domain.EmployeeHistory;
import com.gps.pruebaTecnica.disability.dto.EmployeeHistoryRequestDTO;
import com.gps.pruebaTecnica.disability.dto.RetirementRequestDTO;
import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee-history")
@Tag(name = "Employee History", description = "API para gestión del historial de empleados")
public class EmployeeHistoryController {

    private final EmployeeHistoryService employeeHistoryService;

    public EmployeeHistoryController(EmployeeHistoryService employeeHistoryService) {
        this.employeeHistoryService = employeeHistoryService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar historial de empleado", description = "Agrega un nuevo registro al historial de un empleado")
    public ResponseEntity<ApiResponseDTO<EmployeeHistory>> addEmployeeHistory(@Valid @RequestBody EmployeeHistoryRequestDTO dto) {
        try {
            EmployeeHistory history = employeeHistoryService.addEmployeeHistory(dto);

            return ResponseEntity.ok(ApiResponseDTO.<EmployeeHistory>builder()
                    .success(true)
                    .message("Historial de empleado agregado exitosamente")
                    .data(history)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<EmployeeHistory>builder()
                    .success(false)
                    .message("Error al agregar el historial de empleado: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{documentType}/{documentNumber}")
    @Operation(summary = "Obtener historial de empleado", description = "Obtiene el historial completo de un empleado")
    public ResponseEntity<ApiResponseDTO<List<EmployeeHistory>>> getEmployeeHistory(
            @PathVariable String documentType,
            @PathVariable String documentNumber) {
        try {
            List<EmployeeHistory> history = employeeHistoryService.getEmployeeHistory(documentNumber, documentType);

            return ResponseEntity.ok(ApiResponseDTO.<List<EmployeeHistory>>builder()
                    .success(true)
                    .message("Historial de empleado")
                    .data(history)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<EmployeeHistory>>builder()
                    .success(false)
                    .message("Error al obtener el historial de empleado: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/retirement")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar retiro de empleado", description = "Registra el retiro de un empleado")
    public ResponseEntity<ApiResponseDTO<Void>> registerRetirement(@Valid @RequestBody RetirementRequestDTO dto) {
        try {
            boolean retired = employeeHistoryService.registerRetirement(dto);

            if (!retired) {
                return ResponseEntity.badRequest().body(ApiResponseDTO.<Void>builder()
                        .success(false)
                        .message("No se pudo registrar el retiro. Verifica que el empleado exista y esté activo")
                        .build());
            }

            return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                    .success(true)
                    .message("Retiro de empleado registrado exitosamente")
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<Void>builder()
                    .success(false)
                    .message("Error al registrar el retiro de empleado: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/report/high-salary-multiple-disabilities")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reporte de empleados con alto salario y múltiples incapacidades",
            description = "Obtiene los empleados con salario superior a 1.300.000 y más de 4 incapacidades")
    public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> getHighSalaryMultipleDisabilitiesReport() {
        try {
            BigDecimal minSalary = new BigDecimal("1300000");
            long minDisabilities = 4;

            List<Object[]> results = employeeHistoryService.getEmployeesWithHighSalaryAndMultipleDisabilities(minSalary, minDisabilities);

            List<Map<String, Object>> report = results.stream()
                    .map(row -> Map.of(
                            "documentNumber", row[0],
                            "documentType", row[1],
                            "disabilityCount", row[2]
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponseDTO.<List<Map<String, Object>>>builder()
                    .success(true)
                    .message("Reporte de empleados con alto salario y múltiples incapacidades")
                    .data(report)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponseDTO.<List<Map<String, Object>>>builder()
                    .success(false)
                    .message("Error al generar el reporte: " + e.getMessage())
                    .build());
        }
    }
}
