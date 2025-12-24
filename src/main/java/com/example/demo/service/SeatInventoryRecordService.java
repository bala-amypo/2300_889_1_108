package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;
import java.util.Optional;

public interface SeatInventoryRecordService {

    SeatInventoryRecord createSeatInventory(SeatInventoryRecord record);

    Optional<SeatInventoryRecord> getSeatInventoryById(Long id);

    List<SeatInventoryRecord> getAllSeatInventories();

    SeatInventoryRecord updateSeatInventory(Long id, SeatInventoryRecord updated);

    void deleteSeatInventory(Long id);
}
