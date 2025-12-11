package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;

@Repository
public interface LedgerEntryRepository extends CrudRepository<@NonNull LedgerEntry, @NonNull Long>, LedgerEntryRepositoryCustom {
}
