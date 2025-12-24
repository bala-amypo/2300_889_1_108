package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryRecordServiceImpl implements SeatInventoryRecordService {

    private final SeatInventoryRecordRepository repository;

    public SeatInventoryRecordServiceImpl(SeatInventoryRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public SeatInventoryRecord create(SeatInventoryRecord inventory) {
        return repository.save(inventory);
    }

    @Override
    public SeatInventoryRecord getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat Inventory not found"));
    }

    @Override
    public List<SeatInventoryRecord> getAll() {
        return repository.findAll();
    }

    @Override
    public SeatInventoryRecord update(Long id, SeatInventoryRecord updated) {
        SeatInventoryRecord record = getById(id);

        record.setEventId(updated.getEventId());
        record.setTotalSeats(updated.getTotalSeats());
        record.setRemainingSeats(updated.getRemainingSeats());

        return repository.save(record);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long id, Integer seats) {
        SeatInventoryRecord record = getById(id);
        record.setRemainingSeats(seats);
        return repository.save(record);
    }
}
