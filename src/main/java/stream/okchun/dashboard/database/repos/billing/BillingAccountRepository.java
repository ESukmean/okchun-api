package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;

@Repository
public interface BillingAccountRepository extends CrudRepository<@NonNull BillingAccount, @NonNull Long>, BillingAccountRepositoryCustom {
}
