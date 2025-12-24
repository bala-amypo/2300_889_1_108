package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.EventRecord;
import com.example.demo.model.PricingRule;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRecordRepository;
    private final SeatInventoryRecordRepository seatInventoryRecordRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final DynamicPriceRecordRepository dynamicPriceRecordRepository;
    private final PriceAdjustmentLogRepository priceAdjustmentLogRepository;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRecordRepository,
            SeatInventoryRecordRepository seatInventoryRecordRepository,
            PricingRuleRepository pricingRuleRepository,
            DynamicPriceRecordRepository dynamicPriceRecordRepository,
            PriceAdjustmentLogRepository priceAdjustmentLogRepository
    ) {
        this.eventRecordRepository = eventRecordRepository;
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.dynamicPriceRecordRepository = dynamicPriceRecordRepository;
        this.priceAdjustmentLogRepository = priceAdjustmentLogRepository;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        EventRecord event = eventRecordRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!Boolean.TRUE.equals(event.getActive())) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inv = seatInventoryRecordRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        int remainingSeats = inv.getRemainingSeats();
        int totalSeats = inv.getTotalSeats();
        int soldSeats = totalSeats - remainingSeats;

        LocalDate eventDate = event.getEventDate();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), eventDate);

        double finalPrice = event.getBasePrice();
        String appliedRules = "";

        List<PricingRule> rules = pricingRuleRepository.findByActiveTrue();

        for (PricingRule r : rules) {

            boolean seatMatch =
                    remainingSeats >= r.getMinRemainingSeats()
                            && remainingSeats <= r.getMaxRemainingSeats();

            boolean dayMatch = daysLeft <= r.getDaysBeforeEvent();

            if (seatMatch && dayMatch && Boolean.TRUE.equals(r.getActive())) {
                finalPrice = finalPrice * r.getPriceMultiplier();
                appliedRules += r.getRuleCode() + ",";
            }
        }

        if (appliedRules.isEmpty()) {
            appliedRules = "NONE";
        }

        // Check previous computed price
        double previousPrice = dynamicPriceRecordRepository
                .findFirstByEventIdOrderByComputedAtDesc(eventId)
                .map(DynamicPriceRecord::getComputedPrice)
                .orElse(event.getBasePrice());

        // Save new computed price
        DynamicPriceRecord rec = new DynamicPriceRecord();
        rec.setEventId(eventId);
        rec.setComputedPrice(finalPrice);
        rec.setAppliedRuleCodes(appliedRules);
        rec.setComputedAt(LocalDateTime.now());
        dynamicPriceRecordRepository.save(rec);

        // If changed → log price adjustment
        if (previousPrice != finalPrice) {
            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(previousPrice);
            log.setNewPrice(finalPrice);
            log.setChangedAt(LocalDateTime.now());
            priceAdjustmentLogRepository.save(log);
        }

        return rec;
    }

    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return dynamicPriceRecordRepository
                .findFirstByEventIdOrderByComputedAtDesc(eventId)
                .orElse(null);
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return dynamicPriceRecordRepository.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRecordRepository.findAll();
    }
}
