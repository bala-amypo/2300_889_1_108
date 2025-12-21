package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;

import java.util.List;
import java.util.Optional;

public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository inventoryRepository;
    private final EventRecordRepository eventRepository;

    public SeatInventoryServiceImpl(SeatInventoryRecordRepository inventoryRepository,
                                    EventRecordRepository eventRepository) {
        this.inventoryRepository = inventoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {
        EventRecord event = eventRepository.findById(inventory.getEvent().getId())
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
            throw new BadRequestException("Remaining seats exceed total seats");
        }

        inventory.setEvent(event);
        return inventoryRepository.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long eventId, Integer remainingSeats) {
        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        if (remainingSeats > inventory.getTotalSeats()) {
            throw new BadRequestException("Remaining seats exceed total seats");
        }

        inventory.setRemainingSeats(remainingSeats);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return inventoryRepository.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return inventoryRepository.findAll();
    }
}
