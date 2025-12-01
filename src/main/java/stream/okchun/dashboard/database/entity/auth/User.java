package stream.okchun.dashboard.database.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.auth.RegisterException;

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
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "password_hash", nullable = false, length = Integer.MAX_VALUE)
	private String passwordHash;

	@Column(name = "name", length = Integer.MAX_VALUE)
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
}