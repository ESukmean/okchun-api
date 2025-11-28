package stream.okchun.dashboard.database.repos.infra;


import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface PoolReportRepository extends CrudRepository<@NonNull PoolRepository, Long>, PoolReportRepositoryCustom {
}
