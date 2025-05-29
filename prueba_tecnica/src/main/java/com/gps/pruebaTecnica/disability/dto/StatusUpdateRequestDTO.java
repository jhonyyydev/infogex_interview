package com.gps.pruebaTecnica.disability.dto;

import com.gps.pruebaTecnica.disability.domain.DisabilityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private DisabilityStatus newStatus;
}
