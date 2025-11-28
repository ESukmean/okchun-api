package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.billing.CreditLedger;

public interface OrgCreditLedgerRepository extends
		CrudRepository<@NonNull CreditLedger, @NonNull Long>, OrgCreditLedgerRepositoryCustom {

}
