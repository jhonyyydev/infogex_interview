package com.gps.pruebaTecnica.disability.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RetirementRequestDTO {

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;
}
