package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pricing_rules")
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ruleCode;

    private Integer minRemainingSeats;
    private Integer maxRemainingSeats;
    private Integer daysBeforeEvent;

    @Column(nullable = false)
    private double priceMultiplier;

    @Column(nullable = false)
    private boolean active;

    // getters & setters
    public Long getId() { return id; }
    public String getRuleCode() { return ruleCode; }
    public Integer getMinRemainingSeats() { return minRemainingSeats; }
    public Integer getMaxRemainingSeats() { return maxRemainingSeats; }
    public Integer getDaysBeforeEvent() { return daysBeforeEvent; }
    public double getPriceMultiplier() { return priceMultiplier; }
    public boolean isActive() { return active; }

    public void setId(Long id) { this.id = id; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public void setMinRemainingSeats(Integer minRemainingSeats) { this.minRemainingSeats = minRemainingSeats; }
    public void setMaxRemainingSeats(Integer maxRemainingSeats) { this.maxRemainingSeats = maxRemainingSeats; }
    public void setDaysBeforeEvent(Integer daysBeforeEvent) { this.daysBeforeEvent = daysBeforeEvent; }
    public void setPriceMultiplier(double priceMultiplier) { this.priceMultiplier = priceMultiplier; }
    public void setActive(boolean active) { this.active = active; }
}
