package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.EventRecord;
import com.example.demo.model.PricingRule;
import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.repository.PriceAdjustmentLogRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository dynamicPriceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepository,
            SeatInventoryRecordRepository inventoryRepository,
            PricingRuleRepository ruleRepository,
            DynamicPriceRecordRepository dynamicPriceRepository,
            PriceAdjustmentLogRepository logRepository
    ) {
        this.eventRepository = eventRepository;
        this.inventoryRepository = inventoryRepository;
        this.ruleRepository = ruleRepository;
        this.dynamicPriceRepository = dynamicPriceRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Double computeDynamicPrice(Long eventId) {

        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!Boolean.TRUE.equals(event.getActive())) {
            return event.getBasePrice();
        }

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat Inventory Not Found"));

        double price = event.getBasePrice();
        int remainingSeats = inventory.getRemainingSeats();
        LocalDate eventDate = event.getEventDate();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), eventDate);

        List<PricingRule> rules = ruleRepository.findByActiveTrue();

        StringBuilder appliedRules = new StringBuilder();

        for (PricingRule rule : rules) {

            boolean seatCondition = remainingSeats >= rule.getMinRemainingSeats()
                    && remainingSeats <= rule.getMaxRemainingSeats();

            boolean daysCondition = daysLeft <= rule.getDaysBeforeEvent();

            if (seatCondition && daysCondition) {
                price = price * rule.getPriceMultiplier();

                if (appliedRules.length() > 0) {
                    appliedRules.append(",");
                }
                appliedRules.append(rule.getRuleCode());
            }
        }

        // Save Dynamic Price Record
        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(appliedRules.toString());
        dynamicPriceRepository.save(record);

        // Save Log
        PriceAdjustmentLog log = new PriceAdjustmentLog();
        log.setEventId(eventId);
        log.setOldPrice(event.getBasePrice());
        log.setNewPrice(record.getComputedPrice());
        log.setReason(appliedRules.toString());
        logRepository.save(log);

        return price;
    }
}
