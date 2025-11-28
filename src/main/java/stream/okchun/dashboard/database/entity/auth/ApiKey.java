package stream.okchun.dashboard.database.entity.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "api_keys", schema = "auth", indexes = {
		@Index(name = "idx_api_keys_subject", columnList = "subject_type, subject_id"),
		@Index(name = "idx_api_keys_active", columnList = "is_active")
}, uniqueConstraints = {
		@UniqueConstraint(name = "api_keys_key_key", columnNames = {"key"})
})
public class ApiKey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "key", nullable = false, length = 48)
	private String key;

	@Column(name = "type", columnDefinition = "api_key_type not null")
	@Enumerated(EnumType.STRING)
	private ApiKeyType type;

	@Column(name = "subject_type", columnDefinition = "api_key_subject_type not null")
	@Enumerated(EnumType.STRING)
	private ApiKeySubjectType subjectType;

	@Column(name = "subject_id", nullable = false)
	private Long subjectId;

	@ColumnDefault("'{}'")
	@Column(name = "scopes", nullable = false)
	private List<String> scopes;

	@ColumnDefault("true")
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = false;

	@Column(name = "expires_at")
	private OffsetDateTime expiresAt;

	@Column(name = "last_used_at")
	private OffsetDateTime lastUsedAt;
	@Column(name = "revoked_at")
	private OffsetDateTime revokedAt;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
	@Column(name = "notes", length = Integer.MAX_VALUE)
	private String notes;
}