package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "event_records")
public class EventRecord {
    // fields only
}


@Entity
@Table(
    name = "pricing_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = "rule_code")
)
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_code", nullable = false, unique = true)
    private String ruleCode;

    private String description;

    @Column(name = "min_remaining_seats")
    private Integer minRemainingSeats;

    @Column(name = "max_remaining_seats")
    private Integer maxRemainingSeats;

    @Column(name = "days_before_event")
    private Integer daysBeforeEvent;

    @Column(name = "price_multiplier", nullable = false)
    private Double priceMultiplier;

    @Column(nullable = false)
    private Boolean active = true;

    public PricingRule() {}

    public PricingRule(Long id, String ruleCode, String description,
                       Integer minRemainingSeats, Integer maxRemainingSeats,
                       Integer daysBeforeEvent, Double priceMultiplier,
                       Boolean active) {
        this.id = id;
        this.ruleCode = ruleCode;
        this.description = description;
        this.minRemainingSeats = minRemainingSeats;
        this.maxRemainingSeats = maxRemainingSeats;
        this.daysBeforeEvent = daysBeforeEvent;
        this.priceMultiplier = priceMultiplier;
        this.active = active;
    }
}
