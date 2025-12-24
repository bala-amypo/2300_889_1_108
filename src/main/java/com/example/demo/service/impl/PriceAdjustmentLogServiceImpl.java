package com.example.demo.service.impl;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.repository.PriceAdjustmentLogRepository;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAdjustmentLogServiceImpl implements PriceAdjustmentLogService {

    private final PriceAdjustmentLogRepository logRepository;

    public PriceAdjustmentLogServiceImpl(PriceAdjustmentLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public List<PriceAdjustmentLog> getLogsByEventId(Long eventId) {
        return logRepository.findByEventId(eventId);
    }

    @Override
    public PriceAdjustmentLog saveLog(PriceAdjustmentLog log) {
        return logRepository.save(log);
    }
}
