package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.service.PricingRuleService;

import java.util.List;
import java.util.Optional;

public class PricingRuleServiceImpl implements PricingRuleService {

    private final PricingRuleRepository ruleRepository;

    public PricingRuleServiceImpl(PricingRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public PricingRule createRule(PricingRule rule) {
        if (ruleRepository.existsByRuleCode(rule.getRuleCode())) {
            throw new BadRequestException("Duplicate rule code");
        }
        if (rule.getPriceMultiplier() <= 0) {
            throw new BadRequestException("Invalid price multiplier");
        }
        return ruleRepository.save(rule);
    }

    @Override
    public PricingRule updateRule(Long id, PricingRule updatedRule) {
        PricingRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Rule not found"));

        rule.setDescription(updatedRule.getDescription());
        rule.setMinRemainingSeats(updatedRule.getMinRemainingSeats());
        rule.setMaxRemainingSeats(updatedRule.getMaxRemainingSeats());
        rule.setDaysBeforeEvent(updatedRule.getDaysBeforeEvent());
        rule.setPriceMultiplier(updatedRule.getPriceMultiplier());
        rule.setActive(updatedRule.getActive());

        return ruleRepository.save(rule);
    }

    @Override
    public List<PricingRule> getActiveRules() {
        return ruleRepository.findByActiveTrue();
    }

    @Override
    public Optional<PricingRule> getRuleByCode(String ruleCode) {
        return ruleRepository.findByRuleCode(ruleCode);
    }

    @Override
    public List<PricingRule> getAllRules() {
        return ruleRepository.findAll();
    }
}
