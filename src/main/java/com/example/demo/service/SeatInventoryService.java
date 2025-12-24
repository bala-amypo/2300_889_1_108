package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createSeatInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateSeatInventory(Long id, SeatInventoryRecord record);

    void deleteSeatInventory(Long id);

    List<SeatInventoryRecord> getAllSeatInventories();
}
