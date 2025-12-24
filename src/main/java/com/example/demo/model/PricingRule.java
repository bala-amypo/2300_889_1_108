package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pricing_rules")
@Getter
@Setter
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleCode;
    private String description;
    private Boolean active;

    private Integer minRemainingSeats;
    private Integer maxRemainingSeats;

    private Integer daysBeforeEvent;

    private Double priceMultiplier;
}
