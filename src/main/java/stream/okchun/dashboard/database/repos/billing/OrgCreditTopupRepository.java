package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.billing.CreditTopup;

public interface OrgCreditTopupRepository extends CrudRepository<@NonNull CreditTopup, @NonNull Long>, OrgCreditTopupRepositoryCustom {

}
