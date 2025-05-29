package com.gps.pruebaTecnica.shared.exceptions;

import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Formato de datos incorrecto: " + ex.getMostSpecificCause().getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.builder()
                        .success(false)
                        .message("Error de validación de datos")
                        .errors(List.of(new String[]{errorMessage}))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Errores de validación")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("No autenticado: token ausente o inválido")
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Acceso denegado: no tienes permisos para realizar esta acción")
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGeneralException(Exception ex) {
        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message("Error interno del servidor")
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Violación de integridad de datos.";
        String causeDetail = ex.getMostSpecificCause().getMessage();

        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(false)
                .message(message)
                .errors(List.of(causeDetail))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
