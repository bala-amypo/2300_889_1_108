package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-inventory")
public class SeatInventoryRecordController {

    private final SeatInventoryService seatInventoryService;

    public SeatInventoryRecordController(SeatInventoryService seatInventoryService) {
        this.seatInventoryService = seatInventoryService;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> create(@RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(seatInventoryService.createInventory(record));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<SeatInventoryRecord>> getByEvent(@PathVariable long eventId) {
        return ResponseEntity.ok(seatInventoryService.getInventoryByEvent(eventId));
    }
}
