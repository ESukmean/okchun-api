package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationApiKeyRole;

public interface OrganizationApiKeyRoleRepository extends CrudRepository<@NonNull OrganizationApiKeyRole,
		@NonNull Long>, OrganizationApiKeyRoleRepositoryCustom {
}
