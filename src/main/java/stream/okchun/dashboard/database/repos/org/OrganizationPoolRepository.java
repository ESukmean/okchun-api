package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationPool;

public interface OrganizationPoolRepository extends CrudRepository<@NonNull OrganizationPool,
		@NonNull Long>, OrganizationPoolRepositoryCustom {
}
