package com.example.demo.controller;

import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Double> getDynamicPrice(@PathVariable long eventId) {
        return ResponseEntity.ok(service.calculateDynamicPrice(eventId));
    }
}
