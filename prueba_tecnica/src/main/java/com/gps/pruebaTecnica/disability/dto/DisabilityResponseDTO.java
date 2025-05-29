package com.gps.pruebaTecnica.disability.dto;

import com.gps.pruebaTecnica.disability.domain.Disability;
import com.gps.pruebaTecnica.disability.domain.DisabilityStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DisabilityResponseDTO {

    private UUID id;
    private String filingNumber;
    private String documentNumber;
    private String documentType;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private BigDecimal salary;
    private String eps;
    private LocalDate hireDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String email;
    private String phone;
    private String creatorUsername;
    private DisabilityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DisabilityResponseDTO(Disability disability) {
        this.id = disability.getId();
        this.filingNumber = disability.getFilingNumber();
        this.documentNumber = disability.getDocumentNumber();
        this.documentType = disability.getDocumentType();
        this.firstName = disability.getFirstName();
        this.secondName = disability.getSecondName();
        this.firstLastName = disability.getFirstLastName();
        this.secondLastName = disability.getSecondLastName();
        this.salary = disability.getSalary();
        this.eps = disability.getEps();
        this.hireDate = disability.getHireDate();
        this.startDate = disability.getStartDate();
        this.endDate = disability.getEndDate();
        this.email = disability.getEmail();
        this.phone = disability.getPhone();
        this.creatorUsername = disability.getCreator() != null ? disability.getCreator().getUsername() : null;
        this.status = disability.getStatus();
        this.createdAt = disability.getCreatedAt();
        this.updatedAt = disability.getUpdatedAt();
    }
}
