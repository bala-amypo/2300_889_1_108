package com.example.demo.service;

import com.example.demo.model.PriceAdjustmentLog;

import java.util.List;

public interface PriceAdjustmentLogService {

    // Save adjustment log
    PriceAdjustmentLog logAdjustment(PriceAdjustmentLog log);

    // Get logs by event
    List<PriceAdjustmentLog> getAdjustmentsByEvent(Long eventId);

    // Get all logs
    List<PriceAdjustmentLog> getAllAdjustments();
}
