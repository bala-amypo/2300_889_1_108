package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

public class DynamicPriceRecord {

    private Long id;
    private EventRecord event;
    private double computedPrice;
    private LocalDateTime computedAt;
    private List<String> appliedRuleCodes;

    public DynamicPriceRecord() {
    }

    public Long getId() {
        return id;
    }

    public EventRecord getEvent() {
        return event;
    }

    public double getComputedPrice() {
        return computedPrice;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }

    public List<String> getAppliedRuleCodes() {
        return appliedRuleCodes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(EventRecord event) {
        this.event = event;
    }

    public void setComputedPrice(double computedPrice) {
        this.computedPrice = computedPrice;
    }

    public void setComputedAt(LocalDateTime computedAt) {
        this.computedAt = computedAt;
    }

    public void setAppliedRuleCodes(List<String> appliedRuleCodes) {
        this.appliedRuleCodes = appliedRuleCodes;
    }
}
