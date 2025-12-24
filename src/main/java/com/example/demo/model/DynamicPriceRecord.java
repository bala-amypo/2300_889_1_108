package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamic_price_records")
public class DynamicPriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long eventId;
    private double computedPrice;
    private String appliedRuleCodes;

    private LocalDateTime computedAt;

    @PrePersist
    public void prePersist() {
        this.computedAt = LocalDateTime.now();
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setComputedPrice(double computedPrice) {
        this.computedPrice = computedPrice;
    }

    public void setAppliedRuleCodes(String appliedRuleCodes) {
        this.appliedRuleCodes = appliedRuleCodes;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }
}
