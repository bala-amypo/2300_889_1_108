package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepo;
    private final SeatInventoryRecordRepository seatRepo;
    private final PricingRuleRepository ruleRepo;
    private final DynamicPriceRecordRepository priceRepo;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepo,
            SeatInventoryRecordRepository seatRepo,
            PricingRuleRepository ruleRepo,
            DynamicPriceRecordRepository priceRepo
    ) {
        this.eventRepo = eventRepo;
        this.seatRepo = seatRepo;
        this.ruleRepo = ruleRepo;
        this.priceRepo = priceRepo;
    }

    @Override
    public double calculateDynamicPrice(long eventId) {

        EventRecord event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<SeatInventoryRecord> seats = seatRepo.findByEventId(eventId);
        if (seats.isEmpty()) {
            throw new RuntimeException("Seat Inventory not found");
        }

        SeatInventoryRecord inv = seats.get(0);

        double base = event.getBasePrice();
        double sold = inv.getTotalSeats() - inv.getAvailableSeats();
        double demand = sold / inv.getTotalSeats();

        double price = base;

        if (demand > 0.8) price = base * 1.5;
        else if (demand > 0.5) price = base * 1.2;

        DynamicPriceRecord rec = new DynamicPriceRecord();
        rec.setEventId(eventId);
        rec.setComputedPrice(price);
        rec.setComputedAt(LocalDateTime.now());
        priceRepo.save(rec);

        return price;
    }
}
