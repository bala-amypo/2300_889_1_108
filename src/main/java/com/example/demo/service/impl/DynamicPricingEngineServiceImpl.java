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
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat Inventory Not Found"));

        double price = event.getBasePrice();
        int remainingSeats = inventory.getRemainingSeats();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        List<PricingRule> rules = ruleRepository.findByActiveTrue();

        StringBuilder applied = new StringBuilder();

        for (PricingRule r : rules) {

            boolean seatMatch =
                    remainingSeats >= r.getMinRemainingSeats()
                    && remainingSeats <= r.getMaxRemainingSeats();

            boolean dayMatch =
                    daysLeft <= r.getDaysBeforeEvent();

            if (seatMatch && dayMatch) {
                price = price * r.getPriceMultiplier();

                if (applied.length() > 0) applied.append(",");
                applied.append(r.getRuleCode());
            }
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(applied.toString());
        dynamicPriceRepository.save(record);

        PriceAdjustmentLog log = new PriceAdjustmentLog();
        log.setEventId(eventId);
        log.setOldPrice(event.getBasePrice());
        log.setNewPrice(price);
        log.setReason(applied.toString());
        logRepository.save(log);

        return record;
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRepository.findAll();
    }

    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return dynamicPriceRepository
                .findTopByEventIdOrderByIdDesc(eventId)
                .orElseThrow(() -> new RuntimeException("No price found"));
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return dynamicPriceRepository.findByEventId(eventId);
    }
}
