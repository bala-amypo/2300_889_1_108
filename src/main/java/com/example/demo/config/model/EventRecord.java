@Entity
@Table(name = "event_record", uniqueConstraints = @UniqueConstraint(columnNames = "eventCode"))
public class EventRecord {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String eventCode;
private String eventName;
private String venue;
private LocalDate eventDate;
private Double basePrice;
private Boolean active;
private LocalDateTime createdAt;


@PrePersist
void onCreate() { this.createdAt = LocalDateTime.now(); }
}