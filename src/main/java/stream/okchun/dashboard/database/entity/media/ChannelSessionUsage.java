package stream.okchun.dashboard.database.entity.media;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.infra.Pool;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "channel_session_usage", schema = "media", indexes = {
		@Index(name = "idx_session_usage_org_time", columnList = "org_id, from_ts"),
		@Index(name = "idx_session_usage_channel", columnList = "channel_id, session_id")
})
public class ChannelSessionUsage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "channel_id", nullable = false)
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "session_id", nullable = false)
	private ChannelSession session;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pool_id")
	private Pool pool;

	@Column(name = "from_ts", nullable = false)
	private OffsetDateTime fromTs;

	@Column(name = "to_ts", nullable = false)
	private OffsetDateTime toTs;

	@Column(name = "bytes_used", nullable = false)
	private Long bytesUsed;

	@Column(name = "credit_charged", nullable = false, precision = 20, scale = 4)
	private BigDecimal creditCharged;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

}