package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dynamic_price_records")
public class DynamicPriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventRecord event;

    @Column(nullable = false)
    private double computedPrice;

    @ElementCollection
    @CollectionTable(name = "applied_pricing_rules", joinColumns = @JoinColumn(name = "price_record_id"))
    @Column(name = "rule_code")
    private List<String> appliedRuleCodes;

    @Column(nullable = false)
    private LocalDateTime computedAt;

    // getters & setters
    public Long getId() { return id; }
    public EventRecord getEvent() { return event; }
    public double getComputedPrice() { return computedPrice; }
    public List<String> getAppliedRuleCodes() { return appliedRuleCodes; }
    public LocalDateTime getComputedAt() { return computedAt; }

    public void setId(Long id) { this.id = id; }
    public void setEvent(EventRecord event) { this.event = event; }
    public void setComputedPrice(double computedPrice) { this.computedPrice = computedPrice; }
    public void setAppliedRuleCodes(List<String> appliedRuleCodes) { this.appliedRuleCodes = appliedRuleCodes; }
    public void setComputedAt(LocalDateTime computedAt) { this.computedAt = computedAt; }
}
