package com.gps.pruebaTecnica.disability.infrastructure.repository;

import com.gps.pruebaTecnica.disability.domain.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaEmployeeHistoryRepository extends JpaRepository<EmployeeHistory, UUID> {

    List<EmployeeHistory> findByDocumentNumberAndDocumentTypeOrderByStartDateDesc(String documentNumber, String documentType);

    Optional<EmployeeHistory> findByDocumentNumberAndDocumentTypeAndIsActiveTrue(String documentNumber, String documentType);

    @Query("SELECT e.documentNumber, e.documentType, COUNT(d) as disabilityCount " +
            "FROM EmployeeHistory e JOIN Disability d " +
            "ON e.documentNumber = d.documentNumber AND e.documentType = d.documentType " +
            "WHERE e.salary > :minSalary " +
            "GROUP BY e.documentNumber, e.documentType " +
            "HAVING COUNT(d) > :minDisabilities")
    List<Object[]> findEmployeesWithHighSalaryAndMultipleDisabilities(
            @Param("minSalary") BigDecimal minSalary,
            @Param("minDisabilities") long minDisabilities);
}
