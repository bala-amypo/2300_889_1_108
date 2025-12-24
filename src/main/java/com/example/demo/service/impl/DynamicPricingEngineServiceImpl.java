package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

 private final EventRecordRepository eventRepository;
 private final SeatInventoryRecordRepository inventoryRepository;
 private final PricingRuleRepository ruleRepository;
 private final DynamicPriceRecordRepository priceRepository;
 private final PriceAdjustmentLogRepository logRepository;

 public DynamicPricingEngineServiceImpl(EventRecordRepository eventRepository,
                                        SeatInventoryRecordRepository inventoryRepository,
                                        PricingRuleRepository ruleRepository,
                                        DynamicPriceRecordRepository priceRepository,
                                        PriceAdjustmentLogRepository logRepository) {
  this.eventRepository = eventRepository;
  this.inventoryRepository = inventoryRepository;
  this.ruleRepository = ruleRepository;
  this.priceRepository = priceRepository;
  this.logRepository = logRepository;
 }

 @Override
 public DynamicPriceRecord computeDynamicPrice(Long eventId) {

  EventRecord event = eventRepository.findById(eventId).orElseThrow();

  if (!Boolean.TRUE.equals(event.getActive()))
   throw new BadRequestException("Event is not active");

  SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
          .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

  double basePrice = event.getBasePrice();

  List<PricingRule> rules = ruleRepository.findByActiveTrue();

  double finalMultiplier = 1.0;
  List<String> appliedRules = new ArrayList<>();

  int remaining = inventory.getRemainingSeats();
  long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

  for (PricingRule rule : rules) {
   boolean seatMatch = (rule.getMinRemainingSeats() == null || remaining >= rule.getMinRemainingSeats())
           && (rule.getMaxRemainingSeats() == null || remaining <= rule.getMaxRemainingSeats());

   boolean dayMatch = (rule.getDaysBeforeEvent() == null || daysLeft <= rule.getDaysBeforeEvent());

   if (seatMatch && dayMatch) {
    if (rule.getPriceMultiplier() > finalMultiplier) {
     finalMultiplier = rule.getPriceMultiplier();
     appliedRules.add(rule.getRuleCode());
    }
   }
  }

  double finalPrice = basePrice * finalMultiplier;

  DynamicPriceRecord record = new DynamicPriceRecord();
  record.setEventId(eventId);
  record.setComputedPrice(finalPrice);
  record.setAppliedRuleCodes(String.join(",", appliedRules));

  Optional<DynamicPriceRecord> last = priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

  if (last.isPresent() && !Objects.equals(last.get().getComputedPrice(), finalPrice)) {
   PriceAdjustmentLog log = new PriceAdjustmentLog();
   log.setEventId(eventId);
   log.setOldPrice(last.get().getComputedPrice());
   log.setNewPrice(finalPrice);
   log.setReason("Rule triggered");
   logRepository.save(log);
  }

  return priceRepository.save(record);
 }

 @Override
 public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
  return priceRepository.findByEventIdOrderByComputedAtDesc(eventId);
 }

 @Override
 public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
  return priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
 }

 @Override
 public List<DynamicPriceRecord> getAllComputedPrices() {
  return priceRepository.findAll();
 }
}
