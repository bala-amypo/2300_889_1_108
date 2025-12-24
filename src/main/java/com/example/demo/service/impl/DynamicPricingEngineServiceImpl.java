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

    private final EventRecordRepository eventRecordRepository;
    private final SeatInventoryRecordRepository seatInventoryRecordRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final DynamicPriceRecordRepository dynamicPriceRecordRepository;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRecordRepository,
            SeatInventoryRecordRepository seatInventoryRecordRepository,
            PricingRuleRepository pricingRuleRepository,
            DynamicPriceRecordRepository dynamicPriceRecordRepository
    ) {
        this.eventRecordRepository = eventRecordRepository;
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.dynamicPriceRecordRepository = dynamicPriceRecordRepository;
    }

    @Override
    public double calculateDynamicPrice(long eventId) {

        EventRecord event = eventRecordRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event Not Found"));

        List<SeatInventoryRecord> inventoryList =
                seatInventoryRecordRepository.findByEventId(eventId);

        if (inventoryList.isEmpty()) {
            throw new RuntimeException("No Seat Inventory Found");
        }

        SeatInventoryRecord inventory = inventoryList.get(0);

        double basePrice = event.getBasePrice();
        int remainingSeats = inventory.getRemainingSeats();
        LocalDate eventDate = event.getEventDate();

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), eventDate);

        List<PricingRule> rules = pricingRuleRepository.findAll();
        double finalPrice = basePrice;
        String appliedRules = "";

        for (PricingRule r : rules) {

            boolean seatMatch =
                    remainingSeats >= r.getMinRemainingSeats()
                            && remainingSeats <= r.getMaxRemainingSeats();

            boolean dayMatch =
                    daysLeft <= r.getDaysBeforeEvent();

            if (seatMatch && dayMatch && Boolean.TRUE.equals(r.getActive())) {
                finalPrice = finalPrice * r.getPriceMultiplier();
                appliedRules += r.getRuleCode() + ",";
            }
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(finalPrice);
        record.setAppliedRuleCodes(appliedRules.isEmpty() ? "NONE" : appliedRules);

        dynamicPriceRecordRepository.save(record);

        return finalPrice;
    }

    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return dynamicPriceRecordRepository
                .findTopByEventIdOrderByIdDesc(eventId)
                .orElseThrow(() -> new RuntimeException("No price found"));
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return dynamicPriceRecordRepository.findByEventId(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRecordRepository.findAll();
    }
}
