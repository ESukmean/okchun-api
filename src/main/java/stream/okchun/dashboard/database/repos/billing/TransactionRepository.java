package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<@NonNull Transaction, @NonNull Long>, TransactionRepositoryCustom {
}
