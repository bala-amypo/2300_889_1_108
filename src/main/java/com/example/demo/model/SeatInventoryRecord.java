package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seat_inventory")
public class SeatInventoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventRecord event;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer remainingSeats;

    // getters & setters
    public Long getId() { return id; }
    public EventRecord getEvent() { return event; }
    public Integer getTotalSeats() { return totalSeats; }
    public Integer getRemainingSeats() { return remainingSeats; }

    public void setId(Long id) { this.id = id; }
    public void setEvent(EventRecord event) { this.event = event; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public void setRemainingSeats(Integer remainingSeats) { this.remainingSeats = remainingSeats; }
}
