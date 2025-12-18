@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "ruleCode"))
public class PricingRule {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String ruleCode;
private String description;
private Integer minRemainingSeats;
private Integer maxRemainingSeats;
private Integer daysBeforeEvent;
private Double priceMultiplier;
private Boolean active;
}