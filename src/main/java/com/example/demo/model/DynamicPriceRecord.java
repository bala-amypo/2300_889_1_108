package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class DynamicPriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EventRecord event;

    private double computedPrice;

    @ElementCollection
    private List<String> appliedRuleCodes;

    private LocalDateTime computedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EventRecord getEvent() { return event; }
    public void setEvent(EventRecord event) { this.event = event; }

    public double getComputedPrice() { return computedPrice; }
    public void setComputedPrice(double computedPrice) { this.computedPrice = computedPrice; }

    public List<String> getAppliedRuleCodes() { return appliedRuleCodes; }
    public void setAppliedRuleCodes(List<String> appliedRuleCodes) { this.appliedRuleCodes = appliedRuleCodes; }

    public LocalDateTime getComputedAt() { return computedAt; }
    public void setComputedAt(LocalDateTime computedAt) { this.computedAt = computedAt; }
}
