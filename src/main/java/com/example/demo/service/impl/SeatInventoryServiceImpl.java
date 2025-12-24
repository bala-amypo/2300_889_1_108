package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repo;
    private final EventRecordRepository eventRepo;

    public SeatInventoryServiceImpl(SeatInventoryRecordRepository repo,
                                    EventRecordRepository eventRepo) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {

        EventRecord event = eventRepo.findById(record.getEventId())
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (record.getTotalSeats() == null || record.getTotalSeats() <= 0) {
            throw new BadRequestException("Total seats must be > 0");
        }

        if (record.getRemainingSeats() > record.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }

        return repo.save(record);
    }

    //  Tests expect THIS returns LIST
    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(long eventId) {
        return repo.findByEventId(eventId)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    //  Other tests expect SINGLE OBJECT
    @Override
    public SeatInventoryRecord getSingleInventory(long eventId) {
        return repo.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));
    }
}
