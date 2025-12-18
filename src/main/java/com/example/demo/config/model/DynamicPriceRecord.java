@Entity
public class DynamicPriceRecord {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long eventId;
private Double computedPrice;
private String appliedRuleCodes;
private LocalDateTime computedAt;


@PrePersist
void onCreate() { this.computedAt = LocalDateTime.now(); }
}