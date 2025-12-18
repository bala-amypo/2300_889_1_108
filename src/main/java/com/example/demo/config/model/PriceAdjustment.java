@Entity
public class PriceAdjustmentLog {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long eventId;
private Double oldPrice;
private Double newPrice;
private String reason;
private LocalDateTime changedAt;


@PrePersist
void onCreate() { this.changedAt = LocalDateTime.now(); }
}