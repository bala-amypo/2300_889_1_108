package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "price_adjustment_logs")
public class PriceAdjustmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventRecord event;

    @Column(name = "old_price", nullable = false)
    private Double oldPrice;

    @Column(name = "new_price", nullable = false)
    private Double newPrice;

    private String reason;

    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt;

    public PriceAdjustmentLog() {}

    public PriceAdjustmentLog(Long id, EventRecord event,
                              Double oldPrice, Double newPrice,
                              String reason, LocalDateTime changedAt) {
        this.id = id;
        this.event = event;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.reason = reason;
        this.changedAt = changedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}
