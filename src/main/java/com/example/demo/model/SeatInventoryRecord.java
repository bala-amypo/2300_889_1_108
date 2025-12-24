package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seat_inventory_records")
public class SeatInventoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private int totalSeats;
    private int remainingSeats;

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public Long getEventId() {
        return eventId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
