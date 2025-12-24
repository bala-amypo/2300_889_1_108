package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository seatInventoryRecordRepository;
    private final EventRecordRepository eventRecordRepository;

    // REQUIRED CONSTRUCTOR FOR TEST CASES
    public SeatInventoryServiceImpl(SeatInventoryRecordRepository seatInventoryRecordRepository) {
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.eventRecordRepository = null;
    }

    // YOUR NORMAL CONSTRUCTOR (keep it if you already have)
    public SeatInventoryServiceImpl(SeatInventoryRecordRepository seatInventoryRecordRepository,
                                    EventRecordRepository eventRecordRepository) {
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.eventRecordRepository = eventRecordRepository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {
        return seatInventoryRecordRepository.save(record);
    }

    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(long eventId) {
        return seatInventoryRecordRepository.findByEventId(eventId);
    }
}
