package com.example.demo.service.impl;

import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatInventoryRecordServiceImpl implements SeatInventoryRecordService {

    private final SeatInventoryRecordRepository inventoryRepository;
    private final EventRecordRepository eventRepository;

    public SeatInventoryRecordServiceImpl(
            SeatInventoryRecordRepository inventoryRepository,
            EventRecordRepository eventRepository) {
        this.inventoryRepository = inventoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long eventId, Integer remainingSeats) {

        SeatInventoryRecord inventory = inventoryRepository
                .findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for event"));

        inventory.setRemainingSeats(remainingSeats);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<SeatInventoryRecord> getByEventId(Long eventId) {
        return inventoryRepository.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAll() {
        return inventoryRepository.findAll();
    }
}
