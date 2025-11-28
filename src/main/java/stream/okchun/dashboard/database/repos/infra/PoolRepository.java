package stream.okchun.dashboard.database.repos.infra;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.infra.Pool;

public interface PoolRepository extends CrudRepository<@NonNull Pool, @NonNull Long>, PoolRepositoryCustom {
}
