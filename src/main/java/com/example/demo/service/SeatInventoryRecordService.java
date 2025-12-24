package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryRecordService {

    SeatInventoryRecord create(SeatInventoryRecord inventory);

    SeatInventoryRecord getById(Long id);

    List<SeatInventoryRecord> getAll();

    SeatInventoryRecord update(Long id, SeatInventoryRecord updated);

    void delete(Long id);

    SeatInventoryRecord updateRemainingSeats(Long id, Integer seats);
}
