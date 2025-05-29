package com.gps.pruebaTecnica.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiResponseDTO<Void> apiResponse = ApiResponseDTO.<Void>builder()
                .success(false)
                .message("Acceso denegado: no tiene los permisos necesarios")
                .data(null)
                .errors(List.of(accessDeniedException.getMessage()))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
