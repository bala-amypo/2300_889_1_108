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

 private Integer totalSeats;

 private Integer remainingSeats;

 private LocalDateTime updatedAt;

 public SeatInventoryRecord() {}

 public SeatInventoryRecord(Long id, Long eventId, Integer totalSeats,
 Integer remainingSeats, LocalDateTime updatedAt) {
  this.id = id;
  this.eventId = eventId;
  this.totalSeats = totalSeats;
  this.remainingSeats = remainingSeats;
  this.updatedAt = updatedAt;
 }

 @PrePersist
 @PreUpdate
 public void updateTime() {
  this.updatedAt = LocalDateTime.now();
 }

 // getters and setters
}
