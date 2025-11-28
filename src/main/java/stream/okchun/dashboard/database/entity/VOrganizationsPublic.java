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
@Table(name = "v_organizations_public", schema = "public")
public class VOrganizationsPublic {
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = Integer.MAX_VALUE)
	private String name;

	@Column(name = "default_region", length = 32)
	private String defaultRegion;

	@Column(name = "created_at")
	private OffsetDateTime createdAt;

}