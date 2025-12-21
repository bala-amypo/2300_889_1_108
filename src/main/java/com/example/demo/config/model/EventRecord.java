package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
    name = "event_records",
    uniqueConstraints = @UniqueConstraint(columnNames = "event_code")
)
public class EventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_code", nullable = false, unique = true)
    private String eventCode;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String venue;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatInventoryRecord> seatInventories;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DynamicPriceRecord> dynamicPrices;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceAdjustmentLog> priceAdjustmentLogs;

    public EventRecord() {}

    public EventRecord(Long id, String eventCode, String eventName, String venue,
                       LocalDate eventDate, Double basePrice,
                       LocalDateTime createdAt, Boolean active) {
        this.id = id;
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.venue = venue;
        this.eventDate = eventDate;
        this.basePrice = basePrice;
        this.createdAt = createdAt;
        this.active = active;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.active == null) {
            this.active = true;
        }
    }
}
