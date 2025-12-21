package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dynamic-pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService pricingService;

    @Autowired
    public DynamicPricingController(DynamicPricingEngineService pricingService) {
        this.pricingService = pricingService;
    }

    // POST /compute/{eventId} – compute price
    @PostMapping("/compute/{eventId}")
    public DynamicPriceRecord computePrice(@PathVariable Long eventId) {
        return pricingService.computeDynamicPrice(eventId);
    }

    // GET /latest/{eventId} – get latest price
    @GetMapping("/latest/{eventId}")
    public Optional<DynamicPriceRecord> getLatestPrice(@PathVariable Long eventId) {
        return pricingService.getLatestPrice(eventId);
    }

    // GET /history/{eventId} – get price history
    @GetMapping("/history/{eventId}")
    public List<DynamicPriceRecord> getPriceHistory(@PathVariable Long eventId) {
        return pricingService.getPriceHistory(eventId);
    }

    // GET / – list all computed prices
    @GetMapping("/")
    public List<DynamicPriceRecord> getAllPrices() {
        return pricingService.getAllComputedPrices();
    }
}
