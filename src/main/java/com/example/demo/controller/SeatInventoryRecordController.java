package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-inventory")
public class SeatInventoryRecordController {

    private final SeatInventoryRecordService service;

    @Autowired
    public SeatInventoryRecordController(SeatInventoryRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> create(@RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(service.createSeatInventory(record));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryRecord> getById(@PathVariable Long id) {
        return service.getSeatInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAll() {
        return ResponseEntity.ok(service.getAllSeatInventories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatInventoryRecord> update(
            @PathVariable Long id,
            @RequestBody SeatInventoryRecord updated
    ) {
        return ResponseEntity.ok(service.updateSeatInventory(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSeatInventory(id);
        return ResponseEntity.noContent().build();
    }
}
