package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_adjustment_logs")
public class PriceAdjustmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventRecord event;

    private double oldPrice;
    private double newPrice;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime adjustedAt;

    // getters & setters
    public Long getId() { return id; }
    public EventRecord getEvent() { return event; }
    public double getOldPrice() { return oldPrice; }
    public double getNewPrice() { return newPrice; }
    public String getReason() { return reason; }
    public LocalDateTime getAdjustedAt() { return adjustedAt; }

    public void setId(Long id) { this.id = id; }
    public void setEvent(EventRecord event) { this.event = event; }
    public void setOldPrice(double oldPrice) { this.oldPrice = oldPrice; }
    public void setNewPrice(double newPrice) { this.newPrice = newPrice; }
    public void setReason(String reason) { this.reason = reason; }
    public void setAdjustedAt(LocalDateTime adjustedAt) { this.adjustedAt = adjustedAt; }
}
