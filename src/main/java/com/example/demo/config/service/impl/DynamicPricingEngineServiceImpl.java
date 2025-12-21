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

    // 🔴 REQUIRED constructor signature for testing
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

        // 1️⃣ Fetch event
        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 2️⃣ Validate active
        if (!event.getActive()) {
            throw new RuntimeException("Event is not active");
        }

        // 3️⃣ Fetch seat inventory
        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        // 4️⃣ Days before event
        long daysToEvent = ChronoUnit.DAYS.between(
                LocalDate.now(),
                event.getEventDate()
        );

        // 5️⃣ Fetch active pricing rules
        List<PricingRule> activeRules = ruleRepository.findByActiveTrue();

        // 6️⃣ Match rules
        List<PricingRule> matchedRules = activeRules.stream()
                .filter(rule ->
                        inventory.getRemainingSeats() >= rule.getMinRemainingSeats()
                                && inventory.getRemainingSeats() <= rule.getMaxRemainingSeats()
                                && daysToEvent <= rule.getDaysBeforeEvent()
                )
                .collect(Collectors.toList());

        // 7️⃣ Highest multiplier
        double multiplier = matchedRules.stream()
                .max(Comparator.comparing(PricingRule::getPriceMultiplier))
                .map(PricingRule::getPriceMultiplier)
                .orElse(1.0);

        // 8️⃣ Compute price
        double computedPrice = event.getBasePrice() * multiplier;

        // 9️⃣ Create dynamic price record
        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEvent(event);
        record.setComputedPrice(computedPrice);
        record.setAppliedRuleCodes(
                matchedRules.stream()
                        .map(PricingRule::getRuleCode)
                        .collect(Collectors.joining(","))
        );
        record.setComputedAt(LocalDateTime.now());

        // 🔟 Log adjustment if price changed
        Optional<DynamicPriceRecord> previous =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (previous.isPresent() &&
                Math.abs(previous.get().getComputedPrice() - computedPrice) > 0.01) {

            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEvent(event);
            log.setOldPrice(previous.get().getComputedPrice());
            log.setNewPrice(computedPrice);
            log.setReason("Dynamic pricing adjustment");
            log.setChangedAt(LocalDateTime.now());

            logRepository.save(log);
        }

        // 1️⃣1️⃣ Save & return price
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
