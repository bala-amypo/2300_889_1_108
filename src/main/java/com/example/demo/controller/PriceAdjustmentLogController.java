package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/price-adjustments")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService logService;

    public PriceAdjustmentLogController(PriceAdjustmentLogService logService) {
        this.logService = logService;
    }

    // POST /api/price-adjustments
    @PostMapping
    public ResponseEntity<PriceAdjustmentLog> logAdjustment(
            @RequestBody PriceAdjustmentLog log) {

        PriceAdjustmentLog savedLog = logService.logAdjustment(log);
        return ResponseEntity.ok(savedLog);
    }

    // GET /api/price-adjustments/event/{eventId}
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<PriceAdjustmentLog>> getByEvent(
            @PathVariable Long eventId) {

        List<PriceAdjustmentLog> logs =
                logService.getAdjustmentsByEvent(eventId);

        return ResponseEntity.ok(logs);
    }

    // GET /api/price-adjustments
    @GetMapping
    public ResponseEntity<List<PriceAdjustmentLog>> getAll() {
        return ResponseEntity.ok(logService.getAllAdjustments());
    }

    // GET /api/price-adjustments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PriceAdjustmentLog> getById(
            @PathVariable Long id) {

        Optional<PriceAdjustmentLog> log =
                logService.getAllAdjustments()
                        .stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst();

        return log.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
