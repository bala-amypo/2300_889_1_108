package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        long daysBeforeEvent = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        List<PricingRule> matchingRules = ruleRepository.findByActiveTrue()
                .stream()
                .filter(rule ->
                        (rule.getMinRemainingSeats() == null || inventory.getRemainingSeats() >= rule.getMinRemainingSeats()) &&
                        (rule.getMaxRemainingSeats() == null || inventory.getRemainingSeats() <= rule.getMaxRemainingSeats()) &&
                        (rule.getDaysBeforeEvent() == null || daysBeforeEvent <= rule.getDaysBeforeEvent())
                )
                .collect(Collectors.toList());

        double multiplier = matchingRules.stream()
                .mapToDouble(PricingRule::getPriceMultiplier)
                .max()
                .orElse(1.0);

        double newPrice = event.getBasePrice() * multiplier;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEvent(event);
        record.setComputedPrice(newPrice);
        record.setAppliedRuleCodes(
                matchingRules.stream().map(PricingRule::getRuleCode).collect(Collectors.joining(","))
        );

        Optional<DynamicPriceRecord> previous = priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (previous.isPresent() && Double.compare(previous.get().getComputedPrice(), newPrice) != 0) {
            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEvent(event);
            log.setOldPrice(previous.get().getComputedPrice());
            log.setNewPrice(newPrice);
            log.setReason("Dynamic pricing adjustment");
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
