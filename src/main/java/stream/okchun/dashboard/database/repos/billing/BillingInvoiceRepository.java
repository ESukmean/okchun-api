package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;

@Repository
public interface BillingInvoiceRepository extends CrudRepository<@NonNull BillingInvoice, @NonNull Long>,
		BillingInvoiceRepositoryCustom {
}
