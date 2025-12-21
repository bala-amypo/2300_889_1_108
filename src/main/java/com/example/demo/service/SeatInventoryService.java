package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;
import java.util.Optional;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord inventory);

    SeatInventoryRecord updateRemainingSeats(Long eventId, int seats);

    Optional<SeatInventoryRecord> getByEventId(Long eventId);

    List<SeatInventoryRecord> getAllInventories();
}
