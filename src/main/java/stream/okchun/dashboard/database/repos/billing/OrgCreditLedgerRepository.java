package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface OrgCreditLedgerRepository extends
		CrudRepository<@NonNull OrgCreditLedgerRepository, @NonNull Long>, OrgCreditLedgerRepositoryCustom {

}
