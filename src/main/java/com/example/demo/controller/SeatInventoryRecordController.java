package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class SeatInventoryRecordController {

    private final SeatInventoryRecordService inventoryService;

    public SeatInventoryRecordController(SeatInventoryRecordService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // POST /api/inventory
    @PostMapping
    public ResponseEntity<SeatInventoryRecord> createInventory(
            @RequestBody SeatInventoryRecord inventory) {

        SeatInventoryRecord created = inventoryService.createInventory(inventory);
        return ResponseEntity.ok(created);
    }

    // PUT /api/inventory/{eventId}/remaining?seats=100
    @PutMapping("/{eventId}/remaining")
    public ResponseEntity<SeatInventoryRecord> updateRemainingSeats(
            @PathVariable Long eventId,
            @RequestParam int seats) {

        SeatInventoryRecord updated =
                inventoryService.updateRemainingSeats(eventId, seats);

        return ResponseEntity.ok(updated);
    }

    // GET /api/inventory/event/{eventId}
    @GetMapping("/event/{eventId}")
    public ResponseEntity<SeatInventoryRecord> getByEvent(
            @PathVariable Long eventId) {

        return inventoryService.getByEventId(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/inventory
    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }
}
