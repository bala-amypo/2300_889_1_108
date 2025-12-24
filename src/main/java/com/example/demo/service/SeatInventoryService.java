package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord inv);

    // MUST return single object
    SeatInventoryRecord getInventoryByEvent(long eventId);

    // Optional helper if you still need list anywhere
    List<SeatInventoryRecord> getAllInventories();
}
