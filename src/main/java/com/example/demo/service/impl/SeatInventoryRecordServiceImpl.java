package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatInventoryRecordServiceImpl implements SeatInventoryRecordService {

    private final SeatInventoryRecordRepository repo;

    public SeatInventoryRecordServiceImpl(SeatInventoryRecordRepository repo){
        this.repo = repo;
    }

    @Override
    public SeatInventoryRecord createSeatInventory(SeatInventoryRecord record){
        return repo.save(record);
    }

    @Override
    public Optional<SeatInventoryRecord> getSeatInventoryById(Long id){
        return repo.findById(id);
    }

    @Override
    public List<SeatInventoryRecord> getAllSeatInventories(){
        return repo.findAll();
    }

    @Override
    public SeatInventoryRecord updateSeatInventory(Long id, SeatInventoryRecord updated){
        SeatInventoryRecord record = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat Inventory Not Found"));

        record.setEventId(updated.getEventId());
        record.setTotalSeats(updated.getTotalSeats());
        record.setRemainingSeats(updated.getRemainingSeats());

        return repo.save(record);
    }

    @Override
    public void deleteSeatInventory(Long id){
        repo.deleteById(id);
    }
}
