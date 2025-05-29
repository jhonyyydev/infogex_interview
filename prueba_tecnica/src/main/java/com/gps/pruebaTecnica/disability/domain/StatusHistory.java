package com.gps.pruebaTecnica.disability.domain;

import com.gps.pruebaTecnica.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Audited
@Getter
@Setter
@Entity
@Table(name = "status_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disability_id")
    private Disability disability;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private DisabilityStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private DisabilityStatus newStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "changed_by_user_id")
    private User changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public StatusHistory() {
        this.changedAt = LocalDateTime.now();
    }

    public StatusHistory(Disability disability, DisabilityStatus previousStatus,
                         DisabilityStatus newStatus, User changedBy) {
        this.disability = disability;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
    }
}
