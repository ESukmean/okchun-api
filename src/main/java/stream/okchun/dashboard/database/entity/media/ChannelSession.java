package stream.okchun.dashboard.database.entity.media;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.infra.Pool;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "channel_sessions", schema = "media", indexes = {
		@Index(name = "idx_sessions_channel_state", columnList = "channel_id, state"),
		@Index(name = "idx_sessions_channel_time", columnList = "channel_id, started_at")
})
public class ChannelSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "channel_id", nullable = false)
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pool_id")
	private Pool pool;

	@ColumnDefault("uuid_generate_v4()")
	@Column(name = "external_session_id", nullable = false)
	private UUID externalSessionId;

	@ColumnDefault("now()")
	@Column(name = "started_at", nullable = false)
	private OffsetDateTime startedAt;

	@Column(name = "ended_at")
	private OffsetDateTime endedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_user_id")
	private User createdByUser;

	@ColumnDefault("0")
	@Column(name = "total_bytes_up", nullable = false)
	private Long totalBytesUp;

	@ColumnDefault("0")
	@Column(name = "total_bytes_down", nullable = false)
	private Long totalBytesDown;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@ColumnDefault("'LIVE'")
	@Column(name = "state", columnDefinition = "session_state_type not null")
	@Enumerated(EnumType.STRING)
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private ChannelSessionType state;
}