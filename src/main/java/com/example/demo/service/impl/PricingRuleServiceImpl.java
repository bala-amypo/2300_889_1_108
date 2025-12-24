package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.service.PricingRuleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PricingRuleServiceImpl implements PricingRuleService {

 private final PricingRuleRepository ruleRepository;

 public PricingRuleServiceImpl(PricingRuleRepository ruleRepository) {
  this.ruleRepository = ruleRepository;
 }

 @Override
 public PricingRule createRule(PricingRule rule) {
  if (ruleRepository.existsByRuleCode(rule.getRuleCode()))
   throw new BadRequestException("Duplicate rule code");

  if (rule.getPriceMultiplier() == null || rule.getPriceMultiplier() <= 0)
   throw new BadRequestException("Price multiplier must be > 0");

  return ruleRepository.save(rule);
 }

 @Override
 public PricingRule updateRule(Long id, PricingRule updatedRule) {
  PricingRule rule = ruleRepository.findById(id).orElseThrow();
  rule.setDescription(updatedRule.getDescription());
  rule.setActive(updatedRule.getActive());
  rule.setDaysBeforeEvent(updatedRule.getDaysBeforeEvent());
  rule.setMinRemainingSeats(updatedRule.getMinRemainingSeats());
  rule.setMaxRemainingSeats(updatedRule.getMaxRemainingSeats());
  rule.setPriceMultiplier(updatedRule.getPriceMultiplier());
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
