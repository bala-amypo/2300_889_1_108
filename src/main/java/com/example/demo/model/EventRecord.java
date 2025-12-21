package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "event_records")
public class EventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String eventCode;

    @Column(nullable = false)
    private double basePrice;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDate eventDate;

    // getters and setters
    public Long getId() { return id; }
    public String getEventCode() { return eventCode; }
    public double getBasePrice() { return basePrice; }
    public boolean getActive() { return active; }
    public LocalDate getEventDate() { return eventDate; }

    public void setId(Long id) { this.id = id; }
    public void setEventCode(String eventCode) { this.eventCode = eventCode; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setActive(boolean active) { this.active = active; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
}
