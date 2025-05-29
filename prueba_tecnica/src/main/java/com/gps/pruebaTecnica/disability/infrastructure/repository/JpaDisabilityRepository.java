package com.gps.pruebaTecnica.disability.infrastructure.repository;

import com.gps.pruebaTecnica.disability.domain.Disability;
import com.gps.pruebaTecnica.disability.domain.DisabilityStatus;
import com.gps.pruebaTecnica.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDisabilityRepository extends JpaRepository<Disability, UUID> {

    List<Disability> findByCreator(User creator);

    Optional<Disability> findByFilingNumber(String filingNumber);

    List<Disability> findByStatus(DisabilityStatus status);

    @Query("SELECT d FROM Disability d WHERE d.status = :status AND d.startDate >= :startDate AND d.endDate <= :endDate")
    List<Disability> findByStatusAndDateRange(
            @Param("status") DisabilityStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM Disability d WHERE d.documentNumber = :documentNumber AND d.documentType = :documentType")
    List<Disability> findByDocumentNumberAndDocumentType(
            @Param("documentNumber") String documentNumber,
            @Param("documentType") String documentType);
}
