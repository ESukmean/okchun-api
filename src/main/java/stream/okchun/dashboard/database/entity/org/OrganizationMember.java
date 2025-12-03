package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.User;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organization_members", schema = "org", indexes = {
		@Index(name = "uniq_org_members_active", columnList = "org_id, user_id", unique = true),
		@Index(name = "idx_org_members_org", columnList = "org_id"),
		@Index(name = "idx_org_members_user", columnList = "user_id")
})
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMember {
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
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "role", columnDefinition = "org_member_role not null")
	@Enumerated
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private OrganizationMemberRole role;

	@ColumnDefault("true")
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = false;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;


	@JoinColumn(name = "api_key_id", nullable = false)
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	private ApiKey apiKey;

	@PrePersist
	public void prePersist() {
		createdAt = OffsetDateTime.now();
		updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}