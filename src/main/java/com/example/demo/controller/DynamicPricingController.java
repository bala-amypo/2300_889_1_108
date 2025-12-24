package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dynamic-pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    // Compute new price
    @PostMapping("/{eventId}/compute")
    public DynamicPriceRecord compute(@PathVariable Long eventId) {
        return service.computeDynamicPrice(eventId);
    }

    // Get latest computed price
    @GetMapping("/{eventId}/latest")
    public DynamicPriceRecord latest(@PathVariable Long eventId) {
        return service.getLatestPrice(eventId);
    }

    // Get price history
    @GetMapping("/{eventId}/history")
    public List<DynamicPriceRecord> history(@PathVariable Long eventId) {
        return service.getPriceHistory(eventId);
    }

    // Get all computed prices
    @GetMapping("/all")
    public List<DynamicPriceRecord> all() {
        return service.getAllComputedPrices();
    }
}
