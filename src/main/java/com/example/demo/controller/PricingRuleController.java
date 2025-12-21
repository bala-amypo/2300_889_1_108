package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-rules")
public class PricingRuleController {

    private final PricingRuleService pricingRuleService;

    public PricingRuleController(PricingRuleService pricingRuleService) {
        this.pricingRuleService = pricingRuleService;
    }

    // POST /api/pricing-rules - Create rule
    @PostMapping
    public ResponseEntity<PricingRule> createRule(@RequestBody PricingRule rule) {
        PricingRule createdRule = pricingRuleService.createRule(rule);
        return new ResponseEntity<>(createdRule, HttpStatus.CREATED);
    }

    // PUT /api/pricing-rules/{id} - Update rule
    @PutMapping("/{id}")
    public ResponseEntity<PricingRule> updateRule(
            @PathVariable Long id,
            @RequestBody PricingRule rule) {

        PricingRule updatedRule = pricingRuleService.updateRule(id, rule);
        return ResponseEntity.ok(updatedRule);
    }

    // GET /api/pricing-rules/active - List active rules
    @GetMapping("/active")
    public ResponseEntity<List<PricingRule>> getActiveRules() {
        return ResponseEntity.ok(pricingRuleService.getActiveRules());
    }

    // GET /api/pricing-rules/{id} - Get rule by ID
    @GetMapping("/{id}")
    public ResponseEntity<PricingRule> getRuleById(@PathVariable Long id) {
        return pricingRuleService.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pricing-rules - List all rules
    @GetMapping
    public ResponseEntity<List<PricingRule>> getAllRules() {
        return ResponseEntity.ok(pricingRuleService.getAllRules());
    }
}
