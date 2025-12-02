package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.org.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<@NonNull Organization, @NonNull Long>,
		OrganizationRepositoryCustom {
}
