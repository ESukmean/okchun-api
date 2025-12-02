package stream.okchun.dashboard.database.repos.org;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.org.OrganizationMember;

import java.util.List;

public interface OrganizationMemberRepository extends CrudRepository<@NonNull OrganizationMember,
		@NonNull Long>, OrganizationMemberRepositoryCustom {

	@EntityGraph(attributePaths = {"org", "apiKey"})
	public List<OrganizationMember> findAllByUserId(@NonNull Long userId);
}
