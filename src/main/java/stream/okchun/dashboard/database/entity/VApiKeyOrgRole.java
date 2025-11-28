package stream.okchun.dashboard.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.OffsetDateTime;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_api_key_org_roles", schema = "public")
public class VApiKeyOrgRole {
	@Column(name = "mapping_id")
	private Long mappingId;

	@Column(name = "api_key_id")
	private Long apiKeyId;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "org_name", length = Integer.MAX_VALUE)
	private String orgName;
	@Column(name = "mapping_created_at")
	private OffsetDateTime mappingCreatedAt;

/*
 TODO [Reverse Engineering] create field to map the 'api_key_type' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "api_key_type", columnDefinition = "api_key_type")
    private Object apiKeyType;
*/
/*
 TODO [Reverse Engineering] create field to map the 'org_role' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "org_role", columnDefinition = "org_member_role")
    private Object orgRole;
*/
}