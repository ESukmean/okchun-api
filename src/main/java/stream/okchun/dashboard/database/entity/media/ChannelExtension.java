package stream.okchun.dashboard.database.entity.media;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "channel_extensions", schema = "media", indexes = {
		@Index(name = "idx_extensions_channel_type", columnList = "channel_id, type"),
		@Index(name = "idx_extensions_channel", columnList = "channel_id")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelExtension {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "channel_id", nullable = false)
	private Channel channel;

	@Column(name = "type", nullable = false, length = 32)
	private String type;

	@Column(name = "name", length = Integer.MAX_VALUE)
	private String name;

	@Column(name = "config", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> config;

	@ColumnDefault("true")
	@Column(name = "enabled", nullable = false)
	@Builder.Default
	private Boolean enabled = false;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}
}