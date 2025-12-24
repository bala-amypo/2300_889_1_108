package com.example.demo.controller;

import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dynamic-price")
public class DynamicPricingController {

    @Autowired
    private DynamicPricingEngineService service;

    @GetMapping("/{eventId}")
    public ResponseEntity<Double> getDynamicPrice(@PathVariable Long eventId) {
        double price = service.calculateDynamicPrice(eventId);
        return ResponseEntity.ok(price);
    }
}
