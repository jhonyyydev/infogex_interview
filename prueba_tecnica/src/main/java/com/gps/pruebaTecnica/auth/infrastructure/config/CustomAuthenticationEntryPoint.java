package com.gps.pruebaTecnica.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gps.pruebaTecnica.shared.dto.ApiResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiResponseDTO<Void> apiResponse = ApiResponseDTO.<Void>builder()
                .success(false)
                .message("No autorizado: token ausente o inválido")
                .data(null)
                .errors(List.of(authException.getMessage()))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}
