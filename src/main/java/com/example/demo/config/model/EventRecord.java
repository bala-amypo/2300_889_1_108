package com.example.demo.model;

import java.time.LocalDate;

public class EventRecord {

    private Long id;
    private String eventCode;
    private double basePrice;
    private boolean active;
    private LocalDate eventDate;

    public EventRecord() {
    }

    public Long getId() {
        return id;
    }

    public String getEventCode() {
        return eventCode;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public boolean getActive() {
        return active;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}
