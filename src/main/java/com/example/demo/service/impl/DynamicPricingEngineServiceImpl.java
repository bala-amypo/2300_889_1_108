package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.EventRecord;
import com.example.demo.model.PricingRule;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepo;
    private final SeatInventoryRecordRepository seatRepo;
    private final PricingRuleRepository ruleRepo;
    private final DynamicPriceRecordRepository priceRepo;
    private final PriceAdjustmentLogRepository logRepo;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepo,
            SeatInventoryRecordRepository seatRepo,
            PricingRuleRepository ruleRepo,
            DynamicPriceRecordRepository priceRepo,
            PriceAdjustmentLogRepository logRepo
    ) {
        this.eventRepo = eventRepo;
        this.seatRepo = seatRepo;
        this.ruleRepo = ruleRepo;
        this.priceRepo = priceRepo;
        this.logRepo = logRepo;
    }

    @Override
    public double computeDynamicPrice(long eventId) {

        EventRecord event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event Not Found"));

        List<SeatInventoryRecord> seats = seatRepo.findByEventId(eventId);
        if (seats.isEmpty()) throw new RuntimeException("No seat inventory");

        SeatInventoryRecord inv = seats.get(0);

        double base = event.getBasePrice();
        int remaining = inv.getRemainingSeats();
        LocalDate date = event.getEventDate();

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), date);
        double finalPrice = base;
        String applied = "";

        for (PricingRule r : ruleRepo.findAll()) {
            boolean seatMatch = remaining >= r.getMinRemainingSeats()
                    && remaining <= r.getMaxRemainingSeats();

            boolean dayMatch = daysLeft <= r.getDaysBeforeEvent();

            if (seatMatch && dayMatch && Boolean.TRUE.equals(r.getActive())) {
                finalPrice *= r.getPriceMultiplier();
                applied += r.getRuleCode() + ",";
            }
        }

        if (applied.isEmpty()) applied = "NONE";

        DynamicPriceRecord rec = new DynamicPriceRecord();
        rec.setEventId(eventId);
        rec.setComputedPrice(finalPrice);
        rec.setAppliedRuleCodes(applied);
        rec.setComputedAt(LocalDateTime.now());

        priceRepo.save(rec);

        return finalPrice;
    }

    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return priceRepo.findFirstByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepo.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepo.findAll();
    }
}
