package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "webhook_events", schema = "billing", indexes = {
		@Index(name = "idx_webhook_provider_ext", columnList = "provider, external_id")
})
public class WebhookEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "provider", nullable = false, length = 32)
	private String provider;

	@Column(name = "external_id", length = 128)
	private String externalId;

	@Column(name = "event_type", length = 128)
	private String eventType;

	@Column(name = "payload", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> payload;

	@ColumnDefault("now()")
	@Column(name = "received_at", nullable = false)
	private OffsetDateTime receivedAt;
	@Column(name = "processed_at")
	private OffsetDateTime processedAt;

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'PENDING'")
    @Column(name = "status", columnDefinition = "webhook_status not null")
    private Object status;
*/
}