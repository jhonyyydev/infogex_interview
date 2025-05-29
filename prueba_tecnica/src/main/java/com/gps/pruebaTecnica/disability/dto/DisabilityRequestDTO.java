package com.gps.pruebaTecnica.disability.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DisabilityRequestDTO {

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;

    @NotBlank(message = "El primer nombre es obligatorio")
    private String firstName;

    private String secondName;

    @NotBlank(message = "El primer apellido es obligatorio")
    private String firstLastName;

    private String secondLastName;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser un valor positivo")
    private BigDecimal salary;

    @NotBlank(message = "La EPS es obligatoria")
    private String eps;

    @NotNull(message = "La fecha de contratación es obligatoria")
    private LocalDate hireDate;

    @NotNull(message = "La fecha de inicio de incapacidad es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin de incapacidad es obligatoria")
    private LocalDate endDate;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    private String phone;

    private String position;
}
