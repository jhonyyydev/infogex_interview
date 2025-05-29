package com.gps.pruebaTecnica.disability.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Audited
@Getter
@Setter
@Entity
@Table(name = "employee_history")
public class EmployeeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "position")
    private String position;

    @Column(name = "salary", precision = 19, scale = 2)
    private BigDecimal salary;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "retirement_date")
    private LocalDateTime retirementDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EmployeeHistory() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
}
