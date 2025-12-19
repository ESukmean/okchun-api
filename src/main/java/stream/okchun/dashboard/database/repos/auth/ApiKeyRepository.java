package stream.okchun.dashboard.database.repos.auth;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.auth.ApiKey;

@Repository
public interface ApiKeyRepository extends CrudRepository<@NonNull ApiKey, @NonNull Long>,
		ApiKeyRepositoryCustom {
	boolean existsByKey(String key);

	@EntityGraph(attributePaths = {"org"})
	ApiKey findByKey(String key);
}
