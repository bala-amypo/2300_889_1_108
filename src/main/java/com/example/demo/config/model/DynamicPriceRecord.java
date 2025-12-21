package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter



@Entity
@Table(name = "dynamic_price_records")
public class DynamicPriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventRecord event;

    @Column(name = "computed_price", nullable = false)
    private Double computedPrice;

    @Column(name = "applied_rule_codes")
    private String appliedRuleCodes;

    @Column(name = "computed_at", updatable = false)
    private LocalDateTime computedAt;

    public DynamicPriceRecord() {}

    public DynamicPriceRecord(Long id, EventRecord event,
                              Double computedPrice, String appliedRuleCodes,
                              LocalDateTime computedAt) {
        this.id = id;
        this.event = event;
        this.computedPrice = computedPrice;
        this.appliedRuleCodes = appliedRuleCodes;
        this.computedAt = computedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.computedAt = LocalDateTime.now();
    }
}
