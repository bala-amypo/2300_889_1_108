package com.example.demo.repository;

import com.example.demo.model.PriceAdjustmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceAdjustmentLogRepository extends JpaRepository<PriceAdjustmentLog, Long> {
}
private Double oldPrice;

public Double getOldPrice() {
    return oldPrice;
}

public void setOldPrice(Double oldPrice) {
    this.oldPrice = oldPrice;
}

@Entity
@Table(name = "price_adjustment_logs")
public class PriceAdjustmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    private Double oldPrice;      // <-- required
    private Double newPrice;

    private String reason;

    private LocalDateTime createdAt = LocalDateTime.now();
}
