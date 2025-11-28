package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationMember;

public interface OrganizationMemberRepository extends CrudRepository<@NonNull OrganizationMember,
		@NonNull Long>, OrganizationMemberRepositoryCustom {
}
