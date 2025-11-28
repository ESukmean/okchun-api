package stream.okchun.dashboard.database.entity.infra;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "pool_reports", schema = "infra", indexes = {
		@Index(name = "idx_pool_reports_pool_time", columnList = "pool_id, reported_at")
})
public class PoolReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "pool_id", nullable = false)
	private Pool pool;

	@Column(name = "server_id", nullable = false, length = 64)
	private String serverId;

	@Column(name = "reported_at", nullable = false)
	private OffsetDateTime reportedAt;

	@Column(name = "current_sessions")
	private Integer currentSessions;

	@Column(name = "current_bitrate_mbps", precision = 20, scale = 4)
	private BigDecimal currentBitrateMbps;

	@Column(name = "packets_per_second")
	private Long packetsPerSecond;

	@Column(name = "cpu_percent", precision = 5, scale = 2)
	private BigDecimal cpuPercent;

	@Column(name = "mem_percent", precision = 5, scale = 2)
	private BigDecimal memPercent;

	@Column(name = "errors_last_10s")
	private Integer errorsLast10s;

	@ColumnDefault("'{}'")
	@Column(name = "nic_stats", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> nicStats;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

}