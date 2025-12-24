package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event_records")
public class EventRecord {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(unique = true, nullable = false)
 private String eventCode;

 private String eventName;

 private String venue;

 private LocalDate eventDate;

 private Double basePrice;

 private LocalDateTime createdAt;

 private Boolean active = true;

 @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL, orphanRemoval = false)
 private List<SeatInventoryRecord> seatInventories;

 @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL, orphanRemoval = false)
 private List<DynamicPriceRecord> priceRecords;

 @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL, orphanRemoval = false)
 private List<PriceAdjustmentLog> adjustmentLogs;

 public EventRecord() {}

 public EventRecord(Long id, String eventCode, String eventName, String venue,
 LocalDate eventDate, Double basePrice, LocalDateTime createdAt, Boolean active) {
  this.id = id;
  this.eventCode = eventCode;
  this.eventName = eventName;
  this.venue = venue;
  this.eventDate = eventDate;
  this.basePrice = basePrice;
  this.createdAt = createdAt;
  this.active = active;
 }

 @PrePersist
 public void setCreatedAt() {
  this.createdAt = LocalDateTime.now();
  if (this.active == null)
   this.active = true;
 }

 // getters and setters
}
