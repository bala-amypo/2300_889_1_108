package com.example.demo.repository;

import com.example.demo.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    boolean existsByRuleCode(String ruleCode);

    List<PricingRule> findByActiveTrue();

    Optional<PricingRule> findByRuleCode(String ruleCode);   // <-- ADD THIS
}
package com.example.demo.repository;

import com.example.demo.model.PriceAdjustmentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAdjustmentLogRepository extends JpaRepository<PriceAdjustmentLog, Long> {

    List<PriceAdjustmentLog> findByEventId(Long eventId);
}
package com.example.demo.repository;

import com.example.demo.model.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRecordRepository extends JpaRepository<EventRecord, Long> {

    boolean existsByEventCode(String eventCode);

    Optional<EventRecord> findByEventCode(String eventCode);
}
package com.example.demo.repository;

import com.example.demo.model.DynamicPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DynamicPriceRecordRepository extends JpaRepository<DynamicPriceRecord, Long> {

    List<DynamicPriceRecord> findByEventIdOrderByComputedAtDesc(Long eventId);

    Optional<DynamicPriceRecord> findFirstByEventIdOrderByComputedAtDesc(Long eventId);
}
