package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationPoolId;

public interface OrganizationPoolIdRepository extends CrudRepository<@NonNull OrganizationPoolId,
		@NonNull Long>, OrganizationPoolIdRepositoryCustom {
}
