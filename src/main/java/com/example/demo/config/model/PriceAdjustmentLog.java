package com.example.demo.model;

import java.time.LocalDateTime;

public class PriceAdjustmentLog {

    private Long id;
    private EventRecord event;
    private double oldPrice;
    private double newPrice;
    private String reason;
    private LocalDateTime adjustedAt;

    public PriceAdjustmentLog() {
    }

    public Long getId() {
        return id;
    }

    public EventRecord getEvent() {
        return event;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getAdjustedAt() {
        return adjustedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(EventRecord event) {
        this.event = event;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAdjustedAt(LocalDateTime adjustedAt) {
        this.adjustedAt = adjustedAt;
    }
}
