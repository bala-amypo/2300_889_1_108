package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.EventRecord;
import com.example.demo.model.PricingRule;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepo;
    private final SeatInventoryRecordRepository inventoryRepo;
    private final PricingRuleRepository ruleRepo;
    private final DynamicPriceRecordRepository priceRepo;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepo,
            SeatInventoryRecordRepository inventoryRepo,
            PricingRuleRepository ruleRepo,
            DynamicPriceRecordRepository priceRepo
    ) {
        this.eventRepo = eventRepo;
        this.inventoryRepo = inventoryRepo;
        this.ruleRepo = ruleRepo;
        this.priceRepo = priceRepo;
    }

    @Override
    public double calculateDynamicPrice(long eventId) {

        EventRecord event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event Not Found"));

        SeatInventoryRecord inventory = inventoryRepo.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Inventory Not Found"));

        List<PricingRule> rules = ruleRepo.findAll();

        double price = event.getBasePrice();
        double remainingSeats = inventory.getRemainingSeats();

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        StringBuilder appliedRules = new StringBuilder();

        for (PricingRule r : rules) {
            boolean seatsMatch =
                    remainingSeats >= r.getMinRemainingSeats()
                            && remainingSeats <= r.getMaxRemainingSeats();

            boolean daysMatch = daysLeft <= r.getDaysBeforeEvent();

            if (seatsMatch && daysMatch && Boolean.TRUE.equals(r.getActive())) {
                price = price * r.getPriceMultiplier();
                appliedRules.append(r.getRuleCode()).append(",");
            }
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(appliedRules.toString());

        priceRepo.save(record);

        return price;
    }
}
