package com.example.demo.repository;

import com.example.demo.model.SeatInventoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatInventoryRecordRepository extends JpaRepository<SeatInventoryRecord, Long> {

    // MUST return List (because tests expect it)
    List<SeatInventoryRecord> findByEventId(long eventId);
}
