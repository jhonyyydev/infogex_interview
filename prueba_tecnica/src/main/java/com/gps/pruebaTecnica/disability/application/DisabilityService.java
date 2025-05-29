package com.gps.pruebaTecnica.disability.application;

import com.gps.pruebaTecnica.disability.domain.Disability;
import com.gps.pruebaTecnica.disability.domain.DisabilityStatus;
import com.gps.pruebaTecnica.disability.domain.StatusHistory;
import com.gps.pruebaTecnica.disability.dto.DisabilityRequestDTO;
import com.gps.pruebaTecnica.disability.dto.StatusUpdateRequestDTO;
import com.gps.pruebaTecnica.disability.infrastructure.repository.JpaDisabilityRepository;
import com.gps.pruebaTecnica.disability.infrastructure.repository.JpaStatusHistoryRepository;
import com.gps.pruebaTecnica.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DisabilityService {

    private final JpaDisabilityRepository disabilityRepository;
    private final JpaStatusHistoryRepository statusHistoryRepository;

    public DisabilityService(JpaDisabilityRepository disabilityRepository,
                             JpaStatusHistoryRepository statusHistoryRepository) {
        this.disabilityRepository = disabilityRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    @Transactional
    public Disability createDisability(DisabilityRequestDTO dto, User creator) {
        Disability disability = new Disability();
        disability.setDocumentNumber(dto.getDocumentNumber());
        disability.setDocumentType(dto.getDocumentType());
        disability.setFirstName(dto.getFirstName());
        disability.setSecondName(dto.getSecondName());
        disability.setFirstLastName(dto.getFirstLastName());
        disability.setSecondLastName(dto.getSecondLastName());
        disability.setSalary(dto.getSalary());
        disability.setEps(dto.getEps());
        disability.setHireDate(dto.getHireDate());
        disability.setStartDate(dto.getStartDate());
        disability.setEndDate(dto.getEndDate());
        disability.setEmail(dto.getEmail());
        disability.setPhone(dto.getPhone());
        disability.setCreator(creator);
        disability.setStatus(DisabilityStatus.PENDING);

        // Generar número de radicado
        String filingNumber = generateFilingNumber();
        disability.setFilingNumber(filingNumber);

        Disability savedDisability = disabilityRepository.save(disability);

        // Registrar el historial de estado inicial
        StatusHistory initialStatus = new StatusHistory(
                savedDisability,
                null,
                DisabilityStatus.PENDING,
                creator
        );
        statusHistoryRepository.save(initialStatus);

        return savedDisability;
    }

    private String generateFilingNumber() {
        // Formato: RAD-YYYYMMDD-UUID(8 chars)
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return "RAD-" + datePart + "-" + uuidPart;
    }

    public List<Disability> getDisabilitiesByUser(User user) {
        return disabilityRepository.findByCreator(user);
    }

    public Optional<Disability> getDisabilityById(UUID id) {
        return disabilityRepository.findById(id);
    }

    public Optional<Disability> getDisabilityByFilingNumber(String filingNumber) {
        return disabilityRepository.findByFilingNumber(filingNumber);
    }

    @Transactional
    public Optional<Disability> updateDisability(UUID id, DisabilityRequestDTO dto) {
        Optional<Disability> disabilityOpt = disabilityRepository.findById(id);

        if (disabilityOpt.isPresent()) {
            Disability disability = disabilityOpt.get();

            // Solo permitir actualizar si está en estado PENDING
            if (disability.getStatus() != DisabilityStatus.PENDING) {
                return Optional.empty();
            }

            disability.setDocumentNumber(dto.getDocumentNumber());
            disability.setDocumentType(dto.getDocumentType());
            disability.setFirstName(dto.getFirstName());
            disability.setSecondName(dto.getSecondName());
            disability.setFirstLastName(dto.getFirstLastName());
            disability.setSecondLastName(dto.getSecondLastName());
            disability.setSalary(dto.getSalary());
            disability.setEps(dto.getEps());
            disability.setHireDate(dto.getHireDate());
            disability.setStartDate(dto.getStartDate());
            disability.setEndDate(dto.getEndDate());
            disability.setEmail(dto.getEmail());
            disability.setPhone(dto.getPhone());

            return Optional.of(disabilityRepository.save(disability));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<Disability> updateStatus(UUID id, StatusUpdateRequestDTO dto, User updatedBy) {
        Optional<Disability> disabilityOpt = disabilityRepository.findById(id);

        if (disabilityOpt.isPresent()) {
            Disability disability = disabilityOpt.get();
            DisabilityStatus previousStatus = disability.getStatus();
            DisabilityStatus newStatus = dto.getNewStatus();

            // Actualizar el estado
            disability.updateStatus(newStatus);
            Disability updatedDisability = disabilityRepository.save(disability);

            // Registrar el cambio en el historial
            StatusHistory statusHistory = new StatusHistory(
                    updatedDisability,
                    previousStatus,
                    newStatus,
                    updatedBy
            );
            statusHistoryRepository.save(statusHistory);

            return Optional.of(updatedDisability);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteDisability(UUID id) {
        Optional<Disability> disabilityOpt = disabilityRepository.findById(id);

        if (disabilityOpt.isPresent()) {
            Disability disability = disabilityOpt.get();

            // Solo permitir eliminar si está en estado PENDING
            if (disability.getStatus() != DisabilityStatus.PENDING) {
                return false;
            }

            disabilityRepository.delete(disability);
            return true;
        }

        return false;
    }

    public List<StatusHistory> getStatusHistory(UUID disabilityId) {
        Optional<Disability> disabilityOpt = disabilityRepository.findById(disabilityId);

        if (disabilityOpt.isPresent()) {
            return statusHistoryRepository.findByDisabilityOrderByChangedAtDesc(disabilityOpt.get());
        }

        return List.of();
    }

    public List<Disability> getDisabilitiesByStatusAndDateRange(DisabilityStatus status, LocalDate startDate, LocalDate endDate) {
        return disabilityRepository.findByStatusAndDateRange(status, startDate, endDate);
    }
}
