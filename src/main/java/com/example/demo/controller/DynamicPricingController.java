package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    // Compute new price
    @PostMapping("/compute/{eventId}")
    public ResponseEntity<DynamicPriceRecord> computePrice(@PathVariable Long eventId) {
        DynamicPriceRecord record = service.computeDynamicPrice(eventId);
        return ResponseEntity.ok(record);
    }

    // Get latest computed price
    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> getLatest(@PathVariable Long eventId) {
        DynamicPriceRecord record = service.getLatestPrice(eventId);
        return ResponseEntity.ok(record);
    }

    // Get price history
    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> history(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getPriceHistory(eventId));
    }

    // Get all computed prices
    @GetMapping("/all")
    public ResponseEntity<List<DynamicPriceRecord>> all() {
        return ResponseEntity.ok(service.getAllComputedPrices());
    }
}
