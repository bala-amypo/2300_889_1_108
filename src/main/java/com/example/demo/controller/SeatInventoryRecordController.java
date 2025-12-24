package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class SeatInventoryRecordController {

    private final SeatInventoryRecordService service;

    public SeatInventoryRecordController(SeatInventoryRecordService service) {
        this.service = service;
    }

    @PostMapping
    public SeatInventoryRecord create(@RequestBody SeatInventoryRecord record) {
        return service.createInventory(record);
    }

    @PutMapping("/{eventId}/remaining")
    public SeatInventoryRecord updateRemaining(@PathVariable Long eventId,
                                               @RequestParam Integer remaining) {
        return service.updateRemainingSeats(eventId, remaining);
    }

    @GetMapping("/event/{eventId}")
    public SeatInventoryRecord getByEvent(@PathVariable Long eventId) {
        return service.getInventoryByEvent(eventId)
                .orElseThrow();
    }

    @GetMapping
    public List<SeatInventoryRecord> getAll() {
        return service.getAllInventories();
    }
}
