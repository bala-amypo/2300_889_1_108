package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repo;

    public SeatInventoryServiceImpl(SeatInventoryRecordRepository repo) {
        this.repo = repo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {
        return repo.save(record);
    }

    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(long eventId) {
        return repo.findByEventId(eventId);
    }
}
