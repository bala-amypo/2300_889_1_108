package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository priceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepository,
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

        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getActive()) {
            throw new RuntimeException("Event is not active");
        }

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        long daysToEvent = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        List<PricingRule> activeRules = ruleRepository.findByActiveTrue();

        List<PricingRule> matchedRules = activeRules.stream()
                .filter(rule ->
                        inventory.getRemainingSeats() >= rule.getMinRemainingSeats() &&
                        inventory.getRemainingSeats() <= rule.getMaxRemainingSeats() &&
                        daysToEvent <= rule.getDaysBeforeEvent()
                )
                .collect(Collectors.toList());

        double multiplier = matchedRules.stream()
                .max(Comparator.comparing(PricingRule::getPriceMultiplier))
                .map(PricingRule::getPriceMultiplier)
                .orElse(1.0);

        double computedPrice = event.getBasePrice() * multiplier;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEvent(event);
        record.setComputedPrice(computedPrice);

        // convert List<String> properly
        List<String> ruleCodes = matchedRules.stream()
                .map(PricingRule::getRuleCode)
                .collect(Collectors.toList());

        record.setAppliedRuleCodes(ruleCodes);
        record.setComputedAt(LocalDateTime.now());

        Optional<DynamicPriceRecord> lastRecord =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (lastRecord.isPresent() &&
                Math.abs(lastRecord.get().getComputedPrice() - computedPrice) > 0.01) {

            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEvent(event);
            log.setOldPrice(lastRecord.get().getComputedPrice());
            log.setNewPrice(computedPrice);
            log.setReason("Dynamic pricing adjustment");
            log.setAdjustedAt(LocalDateTime.now());

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
