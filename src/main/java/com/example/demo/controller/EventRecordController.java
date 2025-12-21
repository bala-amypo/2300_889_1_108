package com.example.demo.controller;

import com.example.demo.model.EventRecord;
import com.example.demo.service.EventRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventRecordController {

    private final EventRecordService eventService;

    public EventRecordController(EventRecordService eventService) {
        this.eventService = eventService;
    }

    // POST /api/events - Create event
    @PostMapping
    public ResponseEntity<EventRecord> createEvent(
            @RequestBody EventRecord event) {

        EventRecord created = eventService.createEvent(event);
        return ResponseEntity.ok(created);
    }

    // GET /api/events/{id} - Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventRecord> getEventById(
            @PathVariable Long id) {

        EventRecord event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // GET /api/events - List all events
    @GetMapping
    public ResponseEntity<List<EventRecord>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // PUT /api/events/{id}/status - Update active status
    @PutMapping("/{id}/status")
    public ResponseEntity<EventRecord> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        EventRecord updated =
                eventService.updateEventStatus(id, active);

        return ResponseEntity.ok(updated);
    }

    // GET /api/events/lookup/{eventCode} - Lookup by code
    @GetMapping("/lookup/{eventCode}")
    public ResponseEntity<EventRecord> getByCode(
            @PathVariable String eventCode) {

        Optional<EventRecord> event =
                eventService.getEventByCode(eventCode);

        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
