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

    // Compute Price
    @PostMapping("/compute/{eventId}")
    public ResponseEntity<Double> compute(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.calculateDynamicPrice(eventId));
    }

    // Get Latest Price
    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> latest(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getLatestPrice(eventId));
    }

    // Price History
    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> history(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getPriceHistory(eventId));
    }

    // All Prices
    @GetMapping("/all")
    public ResponseEntity<List<DynamicPriceRecord>> all() {
        return ResponseEntity.ok(service.getAllComputedPrices());
    }
}
