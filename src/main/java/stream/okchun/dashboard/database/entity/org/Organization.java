package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import stream.okchun.dashboard.database.entity.auth.User;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organizations", schema = "org", indexes = {
		@Index(name = "idx_organizations_owner", columnList = "owner_user_id")
})
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_user_id", nullable = false)
	private User ownerUser;

	@Column(name = "default_region", length = 32)
	private String defaultRegion;

	@Column(name = "deleted_at")
	private OffsetDateTime deletedAt;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "org_id", nullable = false, length = 16)
	private String orgId;

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