package com.example.demo.service.impl;

import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.service.PricingRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingRuleServiceImpl implements PricingRuleService {

    private final PricingRuleRepository pricingRuleRepository;

    public PricingRuleServiceImpl(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    @Override
    public PricingRule createRule(PricingRule rule) {

        if (pricingRuleRepository.existsByRuleCode(rule.getRuleCode())) {
            throw new RuntimeException("Pricing rule with code already exists");
        }

        return pricingRuleRepository.save(rule);
    }

    @Override
    public PricingRule updateRule(Long id, PricingRule updatedRule) {

        PricingRule existingRule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing rule not found"));

        existingRule.setDescription(updatedRule.getDescription());
        existingRule.setMinRemainingSeats(updatedRule.getMinRemainingSeats());
        existingRule.setMaxRemainingSeats(updatedRule.getMaxRemainingSeats());
        existingRule.setDaysBeforeEvent(updatedRule.getDaysBeforeEvent());
        existingRule.setPriceMultiplier(updatedRule.getPriceMultiplier());
        existingRule.setActive(updatedRule.getActive());

        return pricingRuleRepository.save(existingRule);
    }

    @Override
    public Optional<PricingRule> getRuleById(Long id) {
        return pricingRuleRepository.findById(id);
    }

    @Override
    public List<PricingRule> getActiveRules() {
        return pricingRuleRepository.findByActiveTrue();
    }

    @Override
    public List<PricingRule> getAllRules() {
        return pricingRuleRepository.findAll();
    }
}
