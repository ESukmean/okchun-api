package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.InvoiceLine;

@Repository
public interface InvoiceLineRepository extends CrudRepository<@NonNull InvoiceLine, @NonNull Long>, InvoiceLineRepositoryCustom {
}
