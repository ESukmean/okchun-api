package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.auth.ApiKey;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organization_api_key_roles", schema = "org", indexes = {
		@Index(name = "uniq_org_api_key_role", columnList = "api_key_id, org_id", unique = true),
		@Index(name = "idx_org_api_key_roles_org", columnList = "org_id")
})
public class OrganizationApiKeyRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "api_key_id", nullable = false)
	private ApiKey apiKey;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

/*
 TODO [Reverse Engineering] create field to map the 'role' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "role", columnDefinition = "org_member_role not null")
    private Object role;
*/
}