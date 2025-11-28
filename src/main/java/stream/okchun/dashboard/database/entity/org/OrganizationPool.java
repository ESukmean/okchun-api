package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.infra.Pool;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organization_pools", schema = "infra")
public class OrganizationPool {
	@EmbeddedId
	private OrganizationPoolId id;

	@MapsId("orgId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@MapsId("poolId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "pool_id", nullable = false)
	private Pool pool;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

}