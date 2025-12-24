package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repo;
    private final EventRecordRepository eventRepo;

    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository repo,
            EventRecordRepository eventRepo
    ) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inv) {

        EventRecord event = eventRepo.findById(inv.getEventId())
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (inv.getRemainingSeats() > inv.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }

        return repo.save(inv);
    }

    @Override
    public SeatInventoryRecord getInventoryByEvent(long eventId) {
        return repo.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));
    }
}
