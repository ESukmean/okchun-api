package stream.okchun.dashboard.database.repos.infra;


import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.infra.Pool;

@Repository
public interface PoolReportRepository extends CrudRepository<@NonNull Pool, @NonNull Long>,
		PoolReportRepositoryCustom {
}
