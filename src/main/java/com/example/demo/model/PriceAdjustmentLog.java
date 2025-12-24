package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_adjustment_logs")
public class PriceAdjustmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private Double oldPrice;
    private Double newPrice;

    private LocalDateTime changedAt;

    @PrePersist
    public void prePersist() {
        changedAt = LocalDateTime.now();
    }

    public void setEventId(Long eventId) { this.eventId = eventId; }
    public void setOldPrice(Double oldPrice) { this.oldPrice = oldPrice; }
    public void setNewPrice(Double newPrice) { this.newPrice = newPrice; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
