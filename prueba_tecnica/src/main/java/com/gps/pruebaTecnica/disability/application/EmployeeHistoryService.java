package com.gps.pruebaTecnica.disability.application;

import com.gps.pruebaTecnica.disability.domain.EmployeeHistory;
import com.gps.pruebaTecnica.disability.dto.EmployeeHistoryRequestDTO;
import com.gps.pruebaTecnica.disability.dto.RetirementRequestDTO;
import com.gps.pruebaTecnica.disability.infrastructure.repository.JpaEmployeeHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeHistoryService {

    private final JpaEmployeeHistoryRepository employeeHistoryRepository;

    public EmployeeHistoryService(JpaEmployeeHistoryRepository employeeHistoryRepository) {
        this.employeeHistoryRepository = employeeHistoryRepository;
    }

    @Transactional
    public EmployeeHistory addEmployeeHistory(EmployeeHistoryRequestDTO dto) {
        // Verificar si hay un registro activo para este empleado
        Optional<EmployeeHistory> activeHistoryOpt = employeeHistoryRepository
                .findByDocumentNumberAndDocumentTypeAndIsActiveTrue(dto.getDocumentNumber(), dto.getDocumentType());

        // Si existe un registro activo, lo desactivamos
        if (activeHistoryOpt.isPresent()) {
            EmployeeHistory activeHistory = activeHistoryOpt.get();
            activeHistory.setActive(false);
            activeHistory.setEndDate(dto.getStartDate().minusDays(1));
            employeeHistoryRepository.save(activeHistory);
        }

        // Crear nuevo registro
        EmployeeHistory newHistory = new EmployeeHistory();
        newHistory.setDocumentNumber(dto.getDocumentNumber());
        newHistory.setDocumentType(dto.getDocumentType());
        newHistory.setPosition(dto.getPosition());
        newHistory.setSalary(dto.getSalary());
        newHistory.setStartDate(dto.getStartDate());
        newHistory.setEndDate(dto.getEndDate());
        newHistory.setActive(true);

        return employeeHistoryRepository.save(newHistory);
    }

    public List<EmployeeHistory> getEmployeeHistory(String documentNumber, String documentType) {
        return employeeHistoryRepository.findByDocumentNumberAndDocumentTypeOrderByStartDateDesc(
                documentNumber, documentType);
    }

    @Transactional
    public boolean registerRetirement(RetirementRequestDTO dto) {
        Optional<EmployeeHistory> activeHistoryOpt = employeeHistoryRepository
                .findByDocumentNumberAndDocumentTypeAndIsActiveTrue(dto.getDocumentNumber(), dto.getDocumentType());

        if (activeHistoryOpt.isPresent()) {
            EmployeeHistory activeHistory = activeHistoryOpt.get();
            activeHistory.setActive(false);
            activeHistory.setRetirementDate(LocalDateTime.now());
            employeeHistoryRepository.save(activeHistory);
            return true;
        }

        return false;
    }

    public List<Object[]> getEmployeesWithHighSalaryAndMultipleDisabilities(BigDecimal minSalary, long minDisabilities) {
        return employeeHistoryRepository.findEmployeesWithHighSalaryAndMultipleDisabilities(minSalary, minDisabilities);
    }
}
