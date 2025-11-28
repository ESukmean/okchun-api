package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.billing.CreditAccount;

public interface OrgCreditAccountRepository extends CrudRepository<@NonNull CreditAccount, @NonNull Long>, OrgCreditAccountRepositoryCustom {
}