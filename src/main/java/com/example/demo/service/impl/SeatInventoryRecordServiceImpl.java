package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatInventoryRecordServiceImpl implements SeatInventoryRecordService {

    private final SeatInventoryRecordRepository inventoryRepository;

    public SeatInventoryRecordServiceImpl(SeatInventoryRecordRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long eventId, Integer remainingSeats) {
        SeatInventoryRecord inventory = inventoryRepository
                .findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setRemainingSeats(remainingSeats);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<SeatInventoryRecord> getByEventId(Long eventId) {
        return inventoryRepository.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return inventoryRepository.findAll();
    }
}
