package com.gps.pruebaTecnica.disability.infrastructure.repository;

import com.gps.pruebaTecnica.disability.domain.Disability;
import com.gps.pruebaTecnica.disability.domain.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaStatusHistoryRepository extends JpaRepository<StatusHistory, UUID> {

    List<StatusHistory> findByDisabilityOrderByChangedAtDesc(Disability disability);
}
