package com.example.demo.service;

import com.example.demo.model.PriceAdjustmentLog;
import java.util.List;

public interface PriceAdjustmentLogService {

    List<PriceAdjustmentLog> getLogsByEventId(Long eventId);

    List<PriceAdjustmentLog> getAllAdjustments();

    PriceAdjustmentLog saveLog(PriceAdjustmentLog log);

    PriceAdjustmentLog logAdjustment(PriceAdjustmentLog log);

    List<PriceAdjustmentLog> getAdjustmentsByEvent(Long eventId);
}
