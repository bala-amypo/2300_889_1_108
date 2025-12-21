package com.example.demo.model;

public class SeatInventoryRecord {

    private Long id;
    private Long eventId;
    private Integer totalSeats;
    private Integer remainingSeats;

    public SeatInventoryRecord() {
    }

    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
    }
}
