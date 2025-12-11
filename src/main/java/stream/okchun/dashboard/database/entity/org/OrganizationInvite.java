package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.auth.User;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organization_invites", schema = "org", indexes = {
		@Index(name = "idx_org_invites_org", columnList = "org_id"),
		@Index(name = "idx_org_invites_email", columnList = "email")
}, uniqueConstraints = {
		@UniqueConstraint(name = "organization_invites_code_key", columnNames = {"code"})
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationInvite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
	private String email;

	@Column(name = "code", nullable = false, length = 32)
	private String code;

	@Column(name = "expires_at")
	private OffsetDateTime expiresAt;
	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by_user_id")
	private User acceptedByUser;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	/*
	 TODO [Reverse Engineering] create field to map the 'role' column
	 Available actions: Define target Java type | Uncomment as is | Remove column mapping
	    @Column(name = "role", columnDefinition = "org_member_role not null")
	    private Object role;
	*/
	/*
	 TODO [Reverse Engineering] create field to map the 'status' column
	 Available actions: Define target Java type | Uncomment as is | Remove column mapping
	    @ColumnDefault("'PENDING'")
	    @Column(name = "status", columnDefinition = "invite_status_type not null")
	    private Object status;
	*/
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