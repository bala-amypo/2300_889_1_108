package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-inventory")
public class SeatInventoryRecordController {

    private final SeatInventoryRecordService seatInventoryRecordService;

    public SeatInventoryRecordController(SeatInventoryRecordService seatInventoryRecordService) {
        this.seatInventoryRecordService = seatInventoryRecordService;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> create(@RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(seatInventoryRecordService.createSeatInventory(record));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryRecord> getById(@PathVariable Long id) {
        return seatInventoryRecordService.getSeatInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAll() {
        return ResponseEntity.ok(seatInventoryRecordService.getAllSeatInventories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatInventoryRecord> update(@PathVariable Long id,
                                                      @RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(seatInventoryRecordService.updateSeatInventory(id, record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatInventoryRecordService.deleteSeatInventory(id);
        return ResponseEntity.noContent().build();
    }
}
