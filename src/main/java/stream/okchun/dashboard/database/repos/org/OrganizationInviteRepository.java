package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationInvite;

public interface OrganizationInviteRepository extends CrudRepository<@NonNull OrganizationInvite,
		@NonNull Long>, OrganizationInviteRepositoryCustom {
}
