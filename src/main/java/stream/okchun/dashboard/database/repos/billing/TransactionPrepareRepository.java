package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;

@Repository
public interface TransactionPrepareRepository extends CrudRepository<@NonNull TransactionPrepare, @NonNull Long>, TransactionPrepareRepositoryCustom {
}
