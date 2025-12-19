package stream.okchun.dashboard.database.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "auth")
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Column(name = "password_hash", nullable = false, length = Integer.MAX_VALUE)
	private String passwordHash;

	@Column(name = "email", length = Integer.MAX_VALUE, nullable = false)
	private String email;

	@Column(name = "name", length = 16, nullable = false)
	private String name;

	@Column(name = "locale", length = 16)
	private String locale;

	@Column(name = "time_zone", length = 64)
	private String timeZone;

	@Column(name = "email_verified_at")
	private OffsetDateTime emailVerifiedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "primary_api_key_id")
	private ApiKey primaryApiKey;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		// Set both to the current time zone aware time
		createdAt = OffsetDateTime.now();
		updatedAt = OffsetDateTime.now();
	}

	/**
	 * Set the update timestamp before the entity is updated.
	 */
	@PreUpdate
	protected void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}