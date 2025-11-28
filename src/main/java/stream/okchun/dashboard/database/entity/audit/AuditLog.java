package stream.okchun.dashboard.database.entity.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "audit_logs", schema = "audit", indexes = {
		@Index(name = "idx_audit_org_time", columnList = "org_id, created_at"),
		@Index(name = "idx_audit_actor", columnList = "actor_user_id, created_at")
})
public class AuditLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id")
	private Organization org;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actor_user_id")
	private User actorUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actor_api_key_id")
	private ApiKey actorApiKey;

	@Column(name = "action", nullable = false, length = 64)
	private String action;

	@Column(name = "target_type", length = 32)
	private String targetType;

	@Column(name = "target_id")
	private Long targetId;

	@ColumnDefault("'{}'")
	@Column(name = "metadata", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> metadata;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

}