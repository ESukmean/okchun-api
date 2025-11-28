package stream.okchun.dashboard.database.entity.org;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrganizationPoolId implements Serializable {
	private static final long serialVersionUID = -7607173579395918290L;
	@Column(name = "org_id", nullable = false)
	private Long orgId;

	@Column(name = "pool_id", nullable = false)
	private Long poolId;

	@Override
	public int hashCode() {
		return Objects.hash(poolId, orgId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OrganizationPoolId entity = (OrganizationPoolId) o;
		return Objects.equals(this.poolId, entity.poolId) &&
				Objects.equals(this.orgId, entity.orgId);
	}

}