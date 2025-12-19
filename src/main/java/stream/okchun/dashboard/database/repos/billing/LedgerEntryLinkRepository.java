package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.LedgerEntryLink;

@Repository
public interface LedgerEntryLinkRepository extends CrudRepository<@NonNull LedgerEntryLink, @NonNull Long>,
		LedgerEntryLinkRepositoryCustom {
}
